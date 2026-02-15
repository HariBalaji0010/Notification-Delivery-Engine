package com.notificationDeliveryEngine.entity;

import java.time.LocalDateTime;
import java.util.UUID;

import com.notificationDeliveryEngine.enums.DeliveryStatus;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import lombok.Data;

@Entity
@Table(name = "delivery_history")
@Data
public class DeliveryHistory {

    @Id
    @GeneratedValue
    private UUID id;

    private UUID notificationId;

    private DeliveryStatus status;

    private LocalDateTime updatedAt;

    private String remarks;
}