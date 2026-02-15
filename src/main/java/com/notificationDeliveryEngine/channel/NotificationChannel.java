package com.notificationDeliveryEngine.channel;

import com.notificationDeliveryEngine.entity.Notification;
import com.notificationDeliveryEngine.enums.ChannelType;

public interface NotificationChannel {
	ChannelType getChannelType();
    void send(Notification notification);

}
