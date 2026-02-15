package com.notificationDeliveryEngine.channel;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.notificationDeliveryEngine.enums.ChannelType;

@Component
public class ChannelRegistry {

    private final Map<ChannelType, NotificationChannel> channels;

    public ChannelRegistry(List<NotificationChannel> channelList) {
        this.channels = channelList.stream()
                .collect(Collectors.toMap(
                        NotificationChannel::getChannelType,
                        Function.identity()
                ));
    }

    public NotificationChannel getChannel(ChannelType type) {
        return channels.get(type);
    }
}