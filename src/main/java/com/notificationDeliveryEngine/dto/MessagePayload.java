package com.notificationDeliveryEngine.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Generic message payload used by SenderService
 * Works for EMAIL, WHATSAPP (and future channels)
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MessagePayload {

    /**
     * EMAIL  -> email address
     * WHATSAPP -> mobile number (with country code)
     */
    private String to;

    /**
     * Used only for EMAIL
     * Ignored for WHATSAPP
     */
    private String subject;

    /**
     * Message body (required for all channels)
     */
    private String body;
}