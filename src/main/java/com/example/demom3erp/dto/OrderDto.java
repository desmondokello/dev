package com.example.demom3erp.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class OrderDto {

    private Long id;

    @NotNull(message = "Customer ID is mandatory")
    private Long customerId;

    @Min(value = 0, message = "Total amount must be greater than or equal to zero")
    private double totalAmount;

    @NotNull(message = "Order items are mandatory")
    private List<OrderItemDto> orderItems;

//    private double totalPrice;
//    @Column(name = "total_price", nullable = false, precision = 10, scale = 2)
    private BigDecimal totalPrice;
    private String status;
    private LocalDateTime orderDate;

}


