package com.nexware.orderplacement.model;

public enum OrderStatus {

    CONFIRMED("Order successfully placed and stock reserved"),
    FAILED("Order failed due to insufficient stock or processing error"),
    CANCELLED("Order cancelled by user or system");

    private final String description;

    OrderStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public boolean canBeCancelled() {
        return this == CONFIRMED;
    }
}
