package com.notificationDeliveryEngine.repository;


import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.notificationDeliveryEngine.entity.Notification;


public interface NotificationRepository extends JpaRepository<Notification, UUID> {
}