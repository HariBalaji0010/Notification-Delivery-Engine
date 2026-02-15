package com.notificationDeliveryEngine.dto;


import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

import com.notificationDeliveryEngine.enums.DeliveryStatus;

@Data
@AllArgsConstructor
public class NotificationStatusDTO {
    private DeliveryStatus status;
    private LocalDateTime lastUpdatedAt;
}