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
public class EmailChannel implements NotificationChannel {

    private final SenderService senderService;

    @Override
    public ChannelType getChannelType() {
        return ChannelType.EMAIL;
    }

    @Override
    public void send(Notification notification) {
        log.info("Sending EMAIL notification: {}", notification.getId());

        MessagePayload payload = new MessagePayload();
        payload.setTo(notification.getRecipient());
        payload.setSubject("Notification");
        payload.setBody(notification.getContent());

        senderService.sendEmail(payload);
    }
}