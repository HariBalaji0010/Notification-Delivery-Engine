package com.notificationDeliveryEngine.dto;

import com.notificationDeliveryEngine.enums.ChannelType;
import lombok.Data;

@Data
public class NotificationRequestDTO {
    private String recipient;       // email or mobile number
    private ChannelType channel;    // EMAIL or WHATSAPP
    private String message;         // notification content
}