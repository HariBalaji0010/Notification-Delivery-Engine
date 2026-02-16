package com.notificationDeliveryEngine.dto;

import com.notificationDeliveryEngine.enums.ChannelType;
import lombok.Data;

@Data
public class NotificationRequestDTO {
    private String recipient;       
    private ChannelType channel;    
    private String message;         

}
