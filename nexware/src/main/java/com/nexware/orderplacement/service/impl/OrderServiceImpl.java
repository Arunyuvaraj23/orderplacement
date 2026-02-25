package com.nexware.orderplacement.service.impl;

import com.nexware.orderplacement.dto.OrderRequest;
import com.nexware.orderplacement.dto.OrderResponse;
import com.nexware.orderplacement.exception.InsufficientStockException;
import com.nexware.orderplacement.exception.ProductNotFoundException;
import com.nexware.orderplacement.exception.UserNotFoundException;
import com.nexware.orderplacement.model.Order;
import com.nexware.orderplacement.model.OrderStatus;
import com.nexware.orderplacement.model.Product;
import com.nexware.orderplacement.model.User;
import com.nexware.orderplacement.repository.OrderRepository;
import com.nexware.orderplacement.repository.ProductRepository;
import com.nexware.orderplacement.repository.UserRepository;
import com.nexware.orderplacement.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public OrderResponse placeOrder(OrderRequest request) {

        log.info("OrderServiceImpl placeOrder :: started");

        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> UserNotFoundException.withId(request.getUserId()));

        // Lock product row — blocks concurrent transactions on the same product
        Product product = productRepository.findByIdWithLock(request.getProductId())
                .orElseThrow(() -> ProductNotFoundException.withId(request.getProductId()));

        if (product.getStock() < request.getQuantity()) {
            throw InsufficientStockException.of(product.getStock(), request.getQuantity());
        }

        product.setStock(product.getStock() - request.getQuantity());
        productRepository.save(product);

        log.info("product saved after concurrent call ::"+ product.toString());

        BigDecimal totalAmount = product.getPrice()
                .multiply(BigDecimal.valueOf(request.getQuantity()));

        Order order = new Order();
        order.setUser(user);
        order.setProduct(product);
        order.setQuantity(request.getQuantity());
        order.setTotalAmount(totalAmount);
        order.setStatus(OrderStatus.CONFIRMED);

        Order saved = orderRepository.save(order);
        log.info("Order {} confirmed. Product '{}' stock: {} → {}",
                saved.getId(), product.getName(),
                product.getStock() + request.getQuantity(), product.getStock());

        return new OrderResponse(saved);
    }
}
