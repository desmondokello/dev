package com.example.demom3erp.service;

import com.example.demom3erp.dto.InventoryDto;
import com.example.demom3erp.entity.Inventory;
import com.example.demom3erp.entity.Product;
import com.example.demom3erp.exception.InsufficientInventoryException;
import com.example.demom3erp.exception.InvalidProductException;
import com.example.demom3erp.exception.ResourceNotFoundException;
import com.example.demom3erp.repository.InventoryRepository;
import com.example.demom3erp.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InventoryService {

    private final InventoryRepository inventoryRepository;
    private final ProductRepository productRepository;
    private static final Logger logger = LoggerFactory.getLogger(InventoryService.class);


    // Convert Inventory entity to InventoryDto
    private InventoryDto convertToDto(Inventory inventory) {
        return new InventoryDto(
                inventory.getId(),
                inventory.getProduct().getId(),
                inventory.getQuantity(),
                inventory.getLocation()
        );
    }

    // Convert InventoryDto to Inventory entity
    private Inventory convertToEntity(InventoryDto inventoryDto) {
        Product product = productRepository.findById(inventoryDto.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + inventoryDto.getProductId()));
        return new Inventory(product, inventoryDto.getQuantity(), inventoryDto.getLocation());
    }

    @Transactional(readOnly = true)
    public List<InventoryDto> getAllInventories() {
        logger.info("Fetching all inventories");
        return inventoryRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public InventoryDto getInventoryById(Long inventoryId) {
        logger.info("Fetching inventory with id: {}", inventoryId);
        Inventory inventory = inventoryRepository.findById(inventoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Inventory not found with id: " + inventoryId));
        return convertToDto(inventory);
    }

    @Transactional
    public InventoryDto createInventory(InventoryDto inventoryDto) {
        logger.info("Creating new inventory for product id: {}", inventoryDto.getProductId());
        Inventory inventory = convertToEntity(inventoryDto);
        Inventory savedInventory = inventoryRepository.save(inventory);
        return convertToDto(savedInventory);
    }

    @Transactional
    public InventoryDto updateInventory(Long inventoryId, InventoryDto inventoryDto) {
        logger.info("Updating inventory with id: {}", inventoryId);
        Inventory existingInventory = inventoryRepository.findById(inventoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Inventory not found with id: " + inventoryId));

        if (inventoryDto.getQuantity() < 0) {
            throw new InvalidProductException("Inventory quantity must be non-negative");
        }

        Product product = productRepository.findById(inventoryDto.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + inventoryDto.getProductId()));

        existingInventory.setProduct(product);
        existingInventory.setQuantity(inventoryDto.getQuantity());
        existingInventory.setLocation(inventoryDto.getLocation());

        Inventory updatedInventory = inventoryRepository.save(existingInventory);
        return convertToDto(updatedInventory);
    }

    @Transactional
    public void deleteInventory(Long inventoryId) {
        logger.info("Deleting inventory with id: {}", inventoryId);
        Inventory inventory = inventoryRepository.findById(inventoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Inventory not found with id: " + inventoryId));

        inventory.setDeletedFlag(true);
        inventoryRepository.save(inventory);
    }

    // Updated method to find available stock for a product
    public int getAvailableStockForProduct(Long productId) {
        return inventoryRepository.findAvailableStockByProductId(productId);
    }

    // Method to check if the desired quantity is available for a product
    public boolean isAvailable(Long productId, int quantity) {
        int availableStock = inventoryRepository.findAvailableStockByProductId(productId);
        return availableStock >= quantity;
    }

    @Transactional
    public void reduceStock(Product product, int requiredQuantity) {
        Inventory inventory = inventoryRepository.findByProduct(product)
                .orElseThrow(() -> new ResourceNotFoundException("Inventory not found for product: " + product.getName()));

        int newStock = inventory.getQuantity() - requiredQuantity;
        if (newStock < 0) {
            throw new InsufficientInventoryException("Insufficient stock for product: " + product.getName());
        }

        inventory.setQuantity(newStock);
        inventoryRepository.save(inventory);
    }

}

