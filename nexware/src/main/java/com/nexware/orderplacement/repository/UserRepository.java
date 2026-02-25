package com.nexware.orderplacement.repository;

import com.nexware.orderplacement.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
