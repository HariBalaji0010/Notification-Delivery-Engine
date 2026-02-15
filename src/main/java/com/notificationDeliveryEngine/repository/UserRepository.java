package com.notificationDeliveryEngine.repository;


import org.springframework.data.jpa.repository.JpaRepository;

import com.notificationDeliveryEngine.entity.User;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findByUsername(String username);
}
