package com.example.demom3erp.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class OrderItemDto {

    private Long id;

    @NotNull
    private Long orderId;

    @NotNull
    private Long productId;

    @Min(1)
    private int quantity;

    @Min(0)
    private double price;


}

