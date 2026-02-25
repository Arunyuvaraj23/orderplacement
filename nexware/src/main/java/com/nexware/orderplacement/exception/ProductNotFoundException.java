package com.nexware.orderplacement.exception;

public class ProductNotFoundException extends RuntimeException {

    public ProductNotFoundException(String message) {
        super(message);
    }

    public static ProductNotFoundException withId(Long productId) {
        return new ProductNotFoundException("Product not found with id: " + productId);
    }
}
