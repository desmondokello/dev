package com.example.demom3erp.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class OrderItemDto {

    private Long id;
//
//    @Min(1)
//    private int quantity;

    @NotNull(message = "Order ID is mandatory")
    private Long orderId;

    @NotNull(message = "Product ID is mandatory")
    private Long productId;

    @Min(value = 0, message = "Quantity must be greater than or equal to zero")
    private int quantity;

    @Min(value = 0, message = "Price must be greater than or equal to zero")
    private BigDecimal price;


}

