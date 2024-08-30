package com.example.demom3erp.service;

import com.example.demom3erp.dto.OrderDto;
import com.example.demom3erp.dto.OrderItemDto;
import com.example.demom3erp.entity.*;
import com.example.demom3erp.exception.InsufficientInventoryException;
import com.example.demom3erp.exception.ResourceNotFoundException;
import com.example.demom3erp.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private InventoryRepository inventoryRepository;

    @Autowired
    private OrderItemService orderItemService;

    @Autowired
    private InventoryService inventoryService;


    // Convert Order entity to OrderDto
//    private OrderDto convertToDto(Order order) {
//        List<OrderItemDto> items = order.getItems().stream()
////                .map(this::convertToDto)
//                .map(item -> orderItemService.convertToDto(item))
//                .collect(Collectors.toList());
//
//        return new OrderDto(order.getId(), order.getCustomer().getId(), items, order.getTotal(), order.getStatus());
//    }
    private OrderDto convertToDto(Order order) {
        OrderDto orderDto = new OrderDto();
        orderDto.setId(order.getId());
        orderDto.setCustomerId(order.getCustomer().getId());
        orderDto.setTotalPrice(order.getTotalPrice());
        orderDto.setStatus(order.getStatus());
        orderDto.setOrderDate(order.getOrderDate());

        List<OrderItemDto> orderItems = order.getOrderItems().stream()
                .map(this::convertToOrderItemDto)// Convert each OrderItem to OrderItemDto
                .collect(Collectors.toList());

        orderDto.setOrderItems(orderItems);

        return orderDto;
    }

    // Helper method to convert OrderItem to OrderItemDto
    private OrderItemDto convertToOrderItemDto(OrderItem orderItem) {
        OrderItemDto orderItemDto = new OrderItemDto();
        orderItemDto.setId(orderItem.getId());
        orderItemDto.setProductId(orderItem.getProduct().getId());
        orderItemDto.setQuantity(orderItem.getQuantity());
        orderItemDto.setPrice(orderItem.getPrice());
        return orderItemDto;
    }

////     Convert OrderItem entity to OrderItemDto
//    private OrderItemDto convertToDto(OrderItem orderItem) {
////        return new OrderItemDto(item.getProduct().getId(), item.getQuantity(), item.getPrice());
//        return new OrderItemDto(orderItem.getId(), orderItem.getOrder().getId(), orderItem.getProduct().getId(), orderItem.getQuantity(), orderItem.getPrice());
//}

    // Convert OrderDto to Order entity
    private Order convertToEntity(OrderDto orderDto) {
        Customer customer = customerRepository.findById(orderDto.getCustomerId())
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with id: " + orderDto.getCustomerId()));

        List<OrderItem> items = orderDto.getOrderItems().stream()
                .map(itemDto -> {
                    Product product = productRepository.findById(itemDto.getProductId())
                            .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + itemDto.getProductId()));
                    return new OrderItem(null, product, itemDto.getQuantity(), itemDto.getPrice());
                })
                .collect(Collectors.toList());

        double total = calculateTotal(items);

        return new Order(customer, items, total, orderDto.getStatus());
    }

    // Calculate the total amount for an order
    private double calculateTotal(List<OrderItem> items) {
            return items.stream().mapToDouble(item -> item.getQuantity() * item.getPrice()).sum();
        }

    // Confirm an order by validating stock and changing its status
    @Transactional
    public OrderDto confirmOrder(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + orderId));

        // Validate stock availability for each item
        for (OrderItem item : order.getOrderItems()) {
            Product product = item.getProduct();
            if (product.getStock() < item.getQuantity()) {
                throw new IllegalStateException("Insufficient stock for product: " + product.getName());
            }
            product.setStock(product.getStock() - item.getQuantity());
            productRepository.save(product);
        }

        // Change the order status to confirmed
        order.setStatus("CONFIRMED");
        Order confirmedOrder = orderRepository.save(order);

        return convertToDto(confirmedOrder);
    }

    public List<OrderDto> getAllOrders() {
        return orderRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public OrderDto getOrderById(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + orderId));
        return convertToDto(order);
    }

    // Place an order
    @Transactional
    public OrderDto createOrder(OrderDto orderDto) {
        // Convert DTO to entity
        Order order = convertToEntity(orderDto);

        // Retrieve and set the customer
        Customer customer = customerRepository.findById(orderDto.getCustomerId())
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with id: " + orderDto.getCustomerId()));
        order.setCustomer(customer);

        // Save the order initially to generate its ID
        Order savedOrder = orderRepository.save(order);

        // Initialize list for order items
        List<OrderItem> orderItems = new ArrayList<>();

        // Process OrderItems and update inventory
        for (OrderItemDto itemDto : orderDto.getOrderItems()) {
            // Ensure product exists before checking inventory
            Product product = productRepository.findById(itemDto.getProductId())
                    .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + itemDto.getProductId()));

            // Check inventory for the product
            Inventory inventory = inventoryRepository.findByProductId(itemDto.getProductId())
                    .orElseThrow(() -> new ResourceNotFoundException("Inventory not found for product id: " + itemDto.getProductId()));

            // Check if enough stock is available
            if (inventory.getQuantity() < itemDto.getQuantity()) {
                throw new IllegalArgumentException("Insufficient stock for product id: " + itemDto.getProductId());
            }

            // Deduct the stock from inventory
            inventory.setQuantity(inventory.getQuantity() - itemDto.getQuantity());
            inventoryRepository.save(inventory);

            // Create and add OrderItem
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(savedOrder);
            orderItem.setProduct(product);
            orderItem.setQuantity(itemDto.getQuantity());
            orderItem.setPrice(itemDto.getPrice());
            orderItems.add(orderItem);
        }

        // Set order items to the saved order and save
        savedOrder.setOrderItems(orderItems);
        savedOrder.setTotalPrice(calculateTotal(orderItems)); // Calculate total price for the order
        orderRepository.save(savedOrder);

        return convertToDto(savedOrder);
    }

    // Update an existing order
    @Transactional
    public OrderDto updateOrder(Long orderId, OrderDto orderDto) {
        // Fetch the existing order
        Order existingOrder = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + orderId));

        // Update the existing order details
        existingOrder.setStatus(orderDto.getStatus());

        // Update customer association if needed
        if (!existingOrder.getCustomer().getId().equals(orderDto.getCustomerId())) {
            Customer newCustomer = customerRepository.findById(orderDto.getCustomerId())
                    .orElseThrow(() -> new ResourceNotFoundException("Customer not found with id: " + orderDto.getCustomerId()));
            existingOrder.setCustomer(newCustomer);
        }

        // Update order items
        updateOrderItems(existingOrder, orderDto.getOrderItems());

        // Recalculate total
        double newTotal = calculateTotal(existingOrder.getOrderItems());
        existingOrder.setTotalPrice(newTotal);

        Order updatedOrder = orderRepository.save(existingOrder);
        return convertToDto(updatedOrder);
    }

    private void updateOrderItems(Order existingOrder, List<OrderItemDto> newItems) {
        // Delete old items from the database
        orderItemRepository.deleteAll(existingOrder.getOrderItems());

        // Clear the existing order items from the in-memory list
//        existingOrder.getOrderItems().clear();

        // Add updated items
        List<OrderItem> updatedItems = newItems.stream()
                .map(itemDto -> {
                    Product product = productRepository.findById(itemDto.getProductId())
                            .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + itemDto.getProductId()));

                    boolean isAvailable = inventoryService.isAvailable(product.getId(), itemDto.getQuantity());
                    if (!isAvailable) {
                        throw new InsufficientInventoryException("Insufficient inventory for product: " + product.getName());
                    }

                    return new OrderItem(existingOrder, product, itemDto.getQuantity(), itemDto.getPrice());
                })

                .collect(Collectors.toList());

        existingOrder.setOrderItems(updatedItems);
    }

    // Delete an order
    public void deleteOrder(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + orderId));
        orderRepository.delete(order);
    }
}

