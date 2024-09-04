package com.example.demom3erp.dto;


import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;


// Constructors, Getters, and Setters...
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductDto {

    private Long id;

    @NotBlank(message = "Product name is mandatory")
    private String name;

    @NotBlank(message = "Product description is mandatory")
    private String description;

    @NotNull(message = "Product price is mandatory")
    @Min(value = 0, message = "Product price must be greater than or equal to zero")
//    private double price; // Ensure correct use of DECIMAL
    private BigDecimal price;

    @Min(value = 0, message = "Product stock must be greater than or equal to zero")
    private int stock;


}
