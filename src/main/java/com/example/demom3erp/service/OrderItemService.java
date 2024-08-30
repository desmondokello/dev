package com.example.demom3erp.service;

import com.example.demom3erp.dto.OrderItemDto;
import com.example.demom3erp.entity.Order;
import com.example.demom3erp.entity.OrderItem;
import com.example.demom3erp.entity.Product;
import com.example.demom3erp.exception.ResourceNotFoundException;
import com.example.demom3erp.repository.OrderItemRepository;
import com.example.demom3erp.repository.OrderRepository;
import com.example.demom3erp.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderItemService {

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductRepository productRepository;

    // Convert OrderItem entity to OrderItemDto
    protected OrderItemDto convertToDto(OrderItem orderItem) {
        return new OrderItemDto(orderItem.getId(), orderItem.getOrder().getId(), orderItem.getProduct().getId(), orderItem.getQuantity(), orderItem.getPrice());
    }

    // Convert OrderItemDto to OrderItem entity
    private OrderItem convertToEntity(OrderItemDto orderItemDto) {
        Order order = orderRepository.findById(orderItemDto.getOrderId())
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + orderItemDto.getOrderId()));


        Product product = productRepository.findById(orderItemDto.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + orderItemDto.getProductId()));

        OrderItem orderItem = new OrderItem();
        orderItem.setOrder(order);
        orderItem.setProduct(product); // Set the entire Product entity
//        orderItem.setProductId(orderItemDto.getProductId());
        orderItem.setQuantity(orderItemDto.getQuantity());
        orderItem.setPrice(orderItemDto.getPrice());
        return orderItem;
    }

    // Retrieve order items by order ID
    public List<OrderItemDto> getOrderItemsByOrderId(Long orderId) {
        return orderItemRepository.findByOrderId(orderId).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    // Create a new OrderItem
    @Transactional
    public OrderItemDto createOrderItem(OrderItemDto orderItemDto) {
        // Ensure the associated order exists
        orderRepository.findById(orderItemDto.getOrderId())
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + orderItemDto.getOrderId()));

        // Ensure the associated product exists
        productRepository.findById(orderItemDto.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + orderItemDto.getProductId()));

        OrderItem orderItem = convertToEntity(orderItemDto);
        return convertToDto(orderItemRepository.save(orderItem));
    }

    // Update an existing OrderItem
    @Transactional
    public OrderItemDto updateOrderItem(Long orderItemId, OrderItemDto orderItemDto) {
        // Ensure the associated order exists
        OrderItem existingOrderItem = orderItemRepository.findById(orderItemId)
                .orElseThrow(() -> new ResourceNotFoundException("OrderItem not found with id: " + orderItemId));

        // Ensure the associated product exists
        Product product = productRepository.findById(orderItemDto.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + orderItemDto.getProductId()));

//        existingOrderItem.setProductId(orderItemDto.getProductId());
        existingOrderItem.setProduct(product);
        existingOrderItem.setQuantity(orderItemDto.getQuantity());
        existingOrderItem.setPrice(orderItemDto.getPrice());

        return convertToDto(orderItemRepository.save(existingOrderItem));
    }

    // Delete an OrderItem
    public void deleteOrderItem(Long orderItemId) {
        OrderItem orderItem = orderItemRepository.findById(orderItemId)
                .orElseThrow(() -> new ResourceNotFoundException("OrderItem not found with id: " + orderItemId));
        orderItemRepository.delete(orderItem);
    }
}
