package com.example.demom3erp.controllers;

import com.example.demom3erp.dto.InventoryDto;
import com.example.demom3erp.service.InventoryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inventories")
public class InventoryController {

    @Autowired
    private InventoryService inventoryService;

    @GetMapping
    public List<InventoryDto> getAllInventories() {
        return inventoryService.getAllInventories();
    }

    @GetMapping("/{id}")
    public ResponseEntity<InventoryDto> getInventoryById(@PathVariable Long id) {
        InventoryDto inventoryDto = inventoryService.getInventoryById(id);
        return ResponseEntity.ok(inventoryDto);
    }

    @PostMapping
    public ResponseEntity<InventoryDto> createInventory(@Valid @RequestBody InventoryDto inventoryDto) {
        InventoryDto createdInventory = inventoryService.createInventory(inventoryDto);
        return ResponseEntity.status(201).body(createdInventory);
    }

    @PutMapping("/{id}")
    public ResponseEntity<InventoryDto> updateInventory(@PathVariable Long id, @Valid @RequestBody InventoryDto inventoryDto) {
        InventoryDto updatedInventory = inventoryService.updateInventory(id, inventoryDto);
        return ResponseEntity.ok(updatedInventory);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteInventory(@PathVariable Long id) {
        inventoryService.deleteInventory(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/product/{productId}/stock")
    public ResponseEntity<Integer> getAvailableStockForProduct(@PathVariable Long productId) {
        int availableStock = inventoryService.getAvailableStockForProduct(productId);
        return ResponseEntity.ok(availableStock);
    }


    @GetMapping("/product/{productId}/availability")
    public ResponseEntity<Boolean> checkProductAvailability(@PathVariable Long productId, @RequestParam int quantity) {
        boolean isAvailable = inventoryService.isAvailable(productId, quantity);
        return ResponseEntity.ok(isAvailable);
    }
}

