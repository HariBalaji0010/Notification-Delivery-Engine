package com.notificationDeliveryEngine.entity;

import java.time.LocalDateTime;
import java.util.UUID;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import com.notificationDeliveryEngine.enums.ChannelType;
import com.notificationDeliveryEngine.enums.DeliveryStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import lombok.Data;

@Entity
@Table(name = "notifications")
@Data
public class Notification {

    @Id
    @GeneratedValue
    @JdbcTypeCode(SqlTypes.BINARY)
    @Column(columnDefinition = "BINARY(16)")
    private UUID id;

    private String recipient;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private ChannelType channelType;

    private String content;

    @Enumerated(EnumType.STRING)
    private DeliveryStatus status;

    private LocalDateTime createdAt;
}