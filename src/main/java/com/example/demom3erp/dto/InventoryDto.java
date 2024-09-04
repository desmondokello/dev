package com.example.demom3erp.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// Getters and Setters
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class InventoryDto {

    private Long id;

    @NotNull(message = "Product ID is mandatory")
    private Long productId;

    @Min(value = 0, message = "Quantity must be greater than or equal to zero")
    private int quantity;

    private String location;


}

