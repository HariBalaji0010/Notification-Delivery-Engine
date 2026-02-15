package com.notificationDeliveryEngine.controller;

import java.time.LocalDateTime;
import java.util.Locale;
import java.util.UUID;

import org.springframework.context.MessageSource;
import org.springframework.web.bind.annotation.*;

import com.notificationDeliveryEngine.dto.NotificationResponseDTO;
import com.notificationDeliveryEngine.dto.NotificationStatusDTO;
import com.notificationDeliveryEngine.dto.ScheduledNotificationDTO;
import com.notificationDeliveryEngine.dto.SendNotificationDTO;
import com.notificationDeliveryEngine.dto.UserDTO;
import com.notificationDeliveryEngine.entity.User;
import com.notificationDeliveryEngine.enums.ChannelType;
import com.notificationDeliveryEngine.service.NotificationService;
import com.notificationDeliveryEngine.service.UserService;

@RestController
@RequestMapping("/admin")
public class AdminController {

    private final UserService userService;
    private final NotificationService notificationService;
    private final MessageSource messageSource;

    public AdminController(UserService userService,
                           NotificationService notificationService,
                           MessageSource messageSource) {
        this.userService = userService;
        this.notificationService = notificationService;
        this.messageSource = messageSource;
    }

    // ---------------- CREATE USER (NO AUTH) ----------------
    @PostMapping("/create-user")
    public NotificationResponseDTO createUser(@RequestBody UserDTO userDTO) {

        User user = new User();
        user.setUsername(userDTO.getUsername());
        user.setUserEmailId(userDTO.getUserEmailId());
        user.setUserMobileNumber(userDTO.getUserMobileNumber());
        user.setUserChannelType(
                ChannelType.valueOf(userDTO.getUserChannelType())
        );

        userService.saveUser(user);

        UUID notificationId;

        // Get message body from messages.properties
        String message = messageSource.getMessage(
                "notification.user.created",
                new Object[]{user.getUsername()},
                Locale.ENGLISH
        );

        // Get email/WhatsApp subject from messages.properties
        String subject = messageSource.getMessage(
                "notification.user.account.subject",
                null,
                Locale.ENGLISH
        );

        if (user.getUserChannelType() == ChannelType.EMAIL) {
            notificationId = notificationService.sendEmail(
                    user.getUserEmailId(),
                    subject,
                    message
            );
        } else {
            notificationId = notificationService.sendWhatsapp(
                    user.getUserMobileNumber(),
                    message
            );
        }

        // Response message from messages.properties
        String responseMessage = messageSource.getMessage(
                "response.user.created",
                null,
                Locale.ENGLISH
        );

        return new NotificationResponseDTO(
                responseMessage,
                notificationId
        );
    }

    // ---------------- SEND NOTIFICATION (AUTH REQUIRED) ----------------
    @PostMapping("/send-notification")
    public NotificationResponseDTO sendNotification(
            @RequestBody SendNotificationDTO requestDTO) {

        User user = userService.getUserByUsername(requestDTO.getUsername())
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        UUID notificationId;

        if (user.getUserChannelType() == ChannelType.EMAIL) {
            notificationId = notificationService.sendEmail(
                    user.getUserEmailId(),
                    "Notification", // Can also be moved to messages.properties
                    requestDTO.getMessage()
            );
        } else {
            notificationId = notificationService.sendWhatsapp(
                    user.getUserMobileNumber(),
                    requestDTO.getMessage()
            );
        }

        String responseMessage = messageSource.getMessage(
                "response.notification.sent",
                null,
                Locale.ENGLISH
        );

        return new NotificationResponseDTO(
                responseMessage,
                notificationId
        );
    }

    // ---------------- SCHEDULE NOTIFICATION (AUTH REQUIRED) ----------------
    @PostMapping("/schd-notification")
    public NotificationResponseDTO scheduleNotification(
            @RequestBody ScheduledNotificationDTO dto) {

        // Validate scheduled time
        if (dto.getScheduleTime() == null || dto.getScheduleTime().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Schedule time must be in the future");
        }

        // Fetch user by username
        User user = userService.getUserByUsername(dto.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        UUID notificationId = notificationService.scheduleNotification(
                user,
                dto.getMessage(),
                dto.getScheduleTime()
        );

        String responseMessage = messageSource.getMessage(
                "response.notification.scheduled",
                null,
                Locale.ENGLISH
        );

        return new NotificationResponseDTO(
                responseMessage,
                notificationId
        );
    }

    // ---------------- GET NOTIFICATION STATUS (AUTH REQUIRED) ----------------
    @GetMapping("/status/{notificationId}")
    public NotificationStatusDTO getNotificationStatus(
            @PathVariable UUID notificationId) {

        return notificationService.getStatus(notificationId);
    }
}