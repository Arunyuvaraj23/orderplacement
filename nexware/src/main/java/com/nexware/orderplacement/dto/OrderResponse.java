package com.nexware.orderplacement.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.nexware.orderplacement.model.Order;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
public class OrderResponse {

    private final Long orderId;
    private final Long userId;
    private final String userName;
    private final Long productId;
    private final String productName;
    private final Integer quantity;
    private final BigDecimal totalAmount;
    private final String status;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private final LocalDateTime createdAt;

    public OrderResponse(Order order) {
        this.orderId      = order.getId();
        this.userId       = order.getUser().getId();
        this.userName     = order.getUser().getName();
        this.productId    = order.getProduct().getId();
        this.productName  = order.getProduct().getName();
        this.quantity     = order.getQuantity();
        this.totalAmount  = order.getTotalAmount();
        this.status       = order.getStatus().name();
        this.createdAt    = order.getCreatedAt();
    }
}
