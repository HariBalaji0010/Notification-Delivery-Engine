package com.notificationDeliveryEngine.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class MessagePayload {

    
    private String to;

    
    private String subject;

    
    private String body;

}
