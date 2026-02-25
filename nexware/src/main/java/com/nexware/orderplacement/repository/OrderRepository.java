package com.nexware.orderplacement.repository;

import com.nexware.orderplacement.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
