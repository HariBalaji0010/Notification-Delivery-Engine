package com.notificationDeliveryEngine.dto;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class NotificationResponseDTO {

    private String message;
    private UUID notificationId;
}