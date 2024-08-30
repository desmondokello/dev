package com.example.demom3erp.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class OrderDto {

    private Long id;
    private Long customerId;
//    private List<OrderItemDto> items;
//    private double total;
    private List<OrderItemDto> orderItems;
    private double totalPrice;
    private String status;
    private LocalDateTime orderDate;

}


