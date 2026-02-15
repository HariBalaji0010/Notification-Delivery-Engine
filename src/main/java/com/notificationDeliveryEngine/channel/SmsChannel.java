package com.notificationDeliveryEngine.channel;




import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import com.notificationDeliveryEngine.entity.Notification;
import com.notificationDeliveryEngine.enums.ChannelType;

@Component
@Slf4j
public class SmsChannel implements NotificationChannel {

    @Override
    public ChannelType getChannelType() {
        return ChannelType.SMS;
    }

    @Override
    public void send(Notification notification) {
        log.info("Sending SMS to {}", notification.getRecipient());

        // TODO: Integrate Twilio / SMS provider
        // Simulating success for now

        log.debug("SMS content: {}", notification.getContent());
    }
}
