package com.example.demom3erp.service;

import com.example.demom3erp.dto.InventoryDto;
import com.example.demom3erp.entity.Inventory;
import com.example.demom3erp.entity.Product;
import com.example.demom3erp.exception.InsufficientInventoryException;
import com.example.demom3erp.exception.ResourceNotFoundException;
import com.example.demom3erp.repository.InventoryRepository;
import com.example.demom3erp.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class InventoryService {

    @Autowired
    private InventoryRepository inventoryRepository;

    @Autowired
    private ProductRepository productRepository;

    // Convert Inventory entity to InventoryDto
    private InventoryDto convertToDto(Inventory inventory) {
        return new InventoryDto(inventory.getId(), inventory.getProduct().getId(), inventory.getQuantity(), inventory.getLocation());
    }

    // Convert InventoryDto to Inventory entity
    private Inventory convertToEntity(InventoryDto inventoryDto) {
        Product product = productRepository.findById(inventoryDto.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + inventoryDto.getProductId()));
        return new Inventory(product, inventoryDto.getQuantity(), inventoryDto.getLocation());
    }

    public List<InventoryDto> getAllInventories() {
        return inventoryRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public InventoryDto getInventoryById(Long inventoryId) {
        Inventory inventory = inventoryRepository.findById(inventoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Inventory not found with id: " + inventoryId));
        return convertToDto(inventory);
    }

    public InventoryDto createInventory(InventoryDto inventoryDto) {
        Inventory inventory = convertToEntity(inventoryDto);
        Inventory savedInventory = inventoryRepository.save(inventory);
        return convertToDto(savedInventory);
    }

    @Transactional
    public InventoryDto updateInventory(Long inventoryId, InventoryDto inventoryDto) {
        Inventory existingInventory = inventoryRepository.findById(inventoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Inventory not found with id: " + inventoryId));

        Product product = productRepository.findById(inventoryDto.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + inventoryDto.getProductId()));

        existingInventory.setProduct(product);
        existingInventory.setQuantity(inventoryDto.getQuantity());
        existingInventory.setLocation(inventoryDto.getLocation());

        Inventory updatedInventory = inventoryRepository.save(existingInventory);
        return convertToDto(updatedInventory);
    }

    public void deleteInventory(Long inventoryId) {
        Inventory inventory = inventoryRepository.findById(inventoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Inventory not found with id: " + inventoryId));
        inventoryRepository.delete(inventory);
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

