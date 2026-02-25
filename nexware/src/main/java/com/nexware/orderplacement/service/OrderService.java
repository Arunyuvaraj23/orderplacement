package com.nexware.orderplacement.service;

import com.nexware.orderplacement.dto.OrderRequest;
import com.nexware.orderplacement.dto.OrderResponse;

public interface OrderService {
    OrderResponse placeOrder(OrderRequest request);
}
