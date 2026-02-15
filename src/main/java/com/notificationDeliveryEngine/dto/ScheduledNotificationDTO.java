package com.notificationDeliveryEngine.dto;

import java.time.LocalDateTime;

public class ScheduledNotificationDTO {

    private String username;
    private String message;
    private LocalDateTime scheduleTime;

    // ---------------- Getters & Setters ----------------
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public LocalDateTime getScheduleTime() { return scheduleTime; }
    public void setScheduleTime(LocalDateTime scheduleTime) { this.scheduleTime = scheduleTime; }

    // ---------------- Optional: toString() for logging ----------------
    @Override
    public String toString() {
        return "ScheduledNotificationDTO{" +
                "username='" + username + '\'' +
                ", message='" + message + '\'' +
                ", scheduleTime=" + scheduleTime +
                '}';
    }
}