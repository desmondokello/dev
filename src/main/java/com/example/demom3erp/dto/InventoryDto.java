package com.example.demom3erp.dto;

import jakarta.websocket.server.ServerEndpoint;
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
    private Long productId;
    private int quantity;
    private String location;


}

