package com.notificationDeliveryEngine.dto;

import lombok.Data;

@Data
public class UserDTO {
    private String username;            // User's unique username
    private String userEmailId;         // User's email address
    private String userMobileNumber;    // User's mobile number
    private String userChannelType;     // WHATSAPP or EMAIL
}