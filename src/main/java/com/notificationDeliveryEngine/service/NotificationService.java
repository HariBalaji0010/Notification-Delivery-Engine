package com.notificationDeliveryEngine.service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.UUID;

import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Service;

import com.notificationDeliveryEngine.channel.NotificationChannel;
import com.notificationDeliveryEngine.dto.NotificationRequestDTO;
import com.notificationDeliveryEngine.dto.NotificationStatusDTO;
import com.notificationDeliveryEngine.entity.DeliveryHistory;
import com.notificationDeliveryEngine.entity.Notification;
import com.notificationDeliveryEngine.entity.User;
import com.notificationDeliveryEngine.enums.ChannelType;
import com.notificationDeliveryEngine.enums.DeliveryStatus;
import com.notificationDeliveryEngine.repository.DeliveryHistoryRepository;
import com.notificationDeliveryEngine.repository.NotificationRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final DeliveryHistoryRepository deliveryHistoryRepository;
    private final List<NotificationChannel> channels;
    private final TaskScheduler taskScheduler;


    public UUID send(NotificationRequestDTO dto) {

        Notification notification = new Notification();
        notification.setRecipient(dto.getRecipient());
        notification.setContent(dto.getMessage());
        notification.setChannelType(dto.getChannel());
        notification.setStatus(DeliveryStatus.CREATED);
        notification.setCreatedAt(LocalDateTime.now());
        notificationRepository.save(notification);

        saveHistory(notification.getId(), DeliveryStatus.CREATED, "Notification created");

        NotificationChannel channel = channels.stream()
                .filter(c -> c.getChannelType().equals(dto.getChannel()))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Channel not supported: " + dto.getChannel()));

        try {
            channel.send(notification);
            saveHistory(notification.getId(), DeliveryStatus.SENT, dto.getChannel() + " sent successfully");
        } catch (Exception e) {
            saveHistory(notification.getId(), DeliveryStatus.FAILED, e.getMessage());
        }

        return notification.getId();
    }

    
    public UUID sendEmail(String recipient, String subject, String message) {
        NotificationRequestDTO dto = new NotificationRequestDTO();
        dto.setRecipient(recipient);
        dto.setMessage("Subject: " + subject + "\n" + message);
        dto.setChannel(ChannelType.EMAIL);
        return send(dto);
    }

    
    public UUID sendWhatsapp(String mobileNumber, String message) {
        NotificationRequestDTO dto = new NotificationRequestDTO();
        dto.setRecipient(mobileNumber);
        dto.setMessage(message);
        dto.setChannel(ChannelType.WHATSAPP);
        return send(dto);
    }

    
    public NotificationStatusDTO getStatus(UUID notificationId) {
        DeliveryHistory latest =
                deliveryHistoryRepository.findByNotificationIdOrderByUpdatedAtDesc(notificationId)
                        .stream()
                        .findFirst()
                        .orElseThrow(() -> new RuntimeException("Notification not found"));

        return new NotificationStatusDTO(latest.getStatus(), latest.getUpdatedAt());
    }

    
    private void saveHistory(UUID notificationId, DeliveryStatus status, String remarks) {
        DeliveryHistory history = new DeliveryHistory();
        history.setNotificationId(notificationId);
        history.setStatus(status);
        history.setRemarks(remarks);
        history.setUpdatedAt(LocalDateTime.now());
        deliveryHistoryRepository.save(history);
    }

    private void updateNotificationStatus(UUID notificationId, DeliveryStatus status) {
        notificationRepository.findById(notificationId).ifPresent(n -> {
            n.setStatus(status);
            notificationRepository.save(n);
        });
    }

    
    public UUID scheduleNotification(User user, String message, LocalDateTime scheduleTime) {

        UUID notificationId = UUID.randomUUID();

        
        Notification notification = new Notification();
        notification.setId(notificationId);
        notification.setRecipient(getRecipient(user));
        notification.setContent(message);
        notification.setChannelType(user.getUserChannelType());
        notification.setStatus(DeliveryStatus.CREATED);
        notification.setCreatedAt(LocalDateTime.now());
        notificationRepository.save(notification);

        saveHistory(notificationId, DeliveryStatus.CREATED, "Notification scheduled");

        
        Instant sendTime = scheduleTime.atZone(ZoneId.systemDefault()).toInstant();
        taskScheduler.schedule(() -> sendScheduledNotification(notification, user, message), sendTime);

        return notificationId;
    }

    private void sendScheduledNotification(Notification notification, User user, String message) {
        try {
            updateNotificationStatus(notification.getId(), DeliveryStatus.PROCESSING);

            NotificationChannel channel = channels.stream()
                    .filter(c -> c.getChannelType().equals(user.getUserChannelType()))
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("Channel not supported"));

            notification.setChannelType(user.getUserChannelType());
            notification.setRecipient(getRecipient(user));
            notification.setContent(message);

            channel.send(notification);

            updateNotificationStatus(notification.getId(), DeliveryStatus.SENT);
            saveHistory(notification.getId(), DeliveryStatus.SENT, "Scheduled notification sent successfully");

        } catch (Exception e) {
            updateNotificationStatus(notification.getId(), DeliveryStatus.FAILED);
            saveHistory(notification.getId(), DeliveryStatus.FAILED, "Scheduled notification failed: " + e.getMessage());
        }
    }

    
    private String getRecipient(User user) {
        return user.getUserChannelType() == ChannelType.EMAIL
                ? user.getUserEmailId()
                : user.getUserMobileNumber();
    }

}
