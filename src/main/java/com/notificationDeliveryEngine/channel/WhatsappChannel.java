package com.notificationDeliveryEngine.channel;

import com.notificationDeliveryEngine.dto.MessagePayload;
import com.notificationDeliveryEngine.entity.Notification;
import com.notificationDeliveryEngine.enums.ChannelType;
import com.notificationDeliveryEngine.service.SenderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class WhatsappChannel implements NotificationChannel {

    private final SenderService senderService;

    @Override
    public ChannelType getChannelType() {
        return ChannelType.WHATSAPP;
    }

    @Override
    public void send(Notification notification) {
        log.info("Sending WhatsApp notification to {}", notification.getRecipient());

        MessagePayload payload = new MessagePayload();
        payload.setTo(notification.getRecipient());
        payload.setBody(notification.getContent());

        senderService.sendWhatsapp(payload);
    }
}