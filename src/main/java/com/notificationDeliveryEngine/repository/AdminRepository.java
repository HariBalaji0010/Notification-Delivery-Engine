package com.notificationDeliveryEngine.repository;

import com.notificationDeliveryEngine.entity.Admin;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AdminRepository extends JpaRepository<Admin, Long> {
    Optional<Admin> findByAdminUserName(String adminUserName);
}