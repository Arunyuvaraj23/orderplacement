package com.nexware.orderplacement.controller;

import com.nexware.orderplacement.dto.OrderRequest;
import com.nexware.orderplacement.dto.OrderResponse;
import com.nexware.orderplacement.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<OrderResponse> placeOrder(@Valid @RequestBody OrderRequest request) {
        log.info("Received order request: userId={}, productId={}, quantity={}",
                request.getUserId(), request.getProductId(), request.getQuantity());

        OrderResponse response = orderService.placeOrder(request);

        log.info("Order placed successfully. orderId={}", response.getOrderId());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
