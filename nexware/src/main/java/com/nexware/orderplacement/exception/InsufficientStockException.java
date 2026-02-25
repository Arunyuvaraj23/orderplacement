package com.nexware.orderplacement.exception;

public class InsufficientStockException extends RuntimeException {

    public InsufficientStockException(String message) {
        super(message);
    }

    public static InsufficientStockException of(int available, int requested) {
        return new InsufficientStockException(
                String.format("Insufficient stock. Available: %d, Requested: %d", available, requested));
    }
}
