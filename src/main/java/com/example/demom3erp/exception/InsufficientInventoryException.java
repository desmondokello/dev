package com.example.demom3erp.exception;

public class InsufficientInventoryException extends RuntimeException{
    public InsufficientInventoryException(String message){
        super("Insufficient inventory: "+message);
    }
}
