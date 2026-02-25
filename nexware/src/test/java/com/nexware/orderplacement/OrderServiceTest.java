package com.nexware.orderplacement;

import com.nexware.orderplacement.dto.OrderRequest;
import com.nexware.orderplacement.dto.OrderResponse;
import com.nexware.orderplacement.exception.InsufficientStockException;
import com.nexware.orderplacement.exception.UserNotFoundException;
import com.nexware.orderplacement.model.Product;
import com.nexware.orderplacement.model.User;
import com.nexware.orderplacement.repository.OrderRepository;
import com.nexware.orderplacement.repository.ProductRepository;
import com.nexware.orderplacement.repository.UserRepository;
import com.nexware.orderplacement.service.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class OrderServiceTest {

    @Autowired OrderService orderService;
    @Autowired UserRepository userRepository;
    @Autowired ProductRepository productRepository;
    @Autowired OrderRepository orderRepository;

    private User user;
    private Product product;

    @BeforeEach
    void setUp() {
        orderRepository.deleteAll();
        productRepository.deleteAll();
        userRepository.deleteAll();

        user    = userRepository.save(new User(null, "Test User"));
        product = productRepository.save(new Product(null, "Test Widget", new BigDecimal("20.00"), 10, null));
    }

    @Test
    void placeOrder_success() {
        OrderResponse response = orderService.placeOrder(
                new OrderRequest(user.getId(), product.getId(), 3));

        assertThat(response.getStatus()).isEqualTo("CONFIRMED");
        assertThat(response.getTotalAmount()).isEqualByComparingTo("60.00");

        int remainingStock = productRepository.findById(product.getId()).orElseThrow().getStock();
        assertThat(remainingStock).isEqualTo(7);
    }

    @Test
    void placeOrder_userNotFound_throwsException() {
        assertThatThrownBy(() -> orderService.placeOrder(
                new OrderRequest(9999L, product.getId(), 1)))
                .isInstanceOf(UserNotFoundException.class);
    }

    @Test
    void placeOrder_insufficientStock_throwsException() {
        assertThatThrownBy(() -> orderService.placeOrder(
                new OrderRequest(user.getId(), product.getId(), 99)))
                .isInstanceOf(InsufficientStockException.class);
    }
}
