package com.notificationDeliveryEngine.dto;

import lombok.Data;

@Data
public class SendNotificationDTO {
    private String username;  // Username of the user to send notification
    private String message;   // Notification message content
}