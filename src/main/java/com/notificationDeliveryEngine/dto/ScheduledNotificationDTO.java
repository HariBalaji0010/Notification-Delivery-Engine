package com.notificationDeliveryEngine.dto;

import java.time.LocalDateTime;

public class ScheduledNotificationDTO {

    private String username;
    private String message;
    private LocalDateTime scheduleTime;

    
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public LocalDateTime getScheduleTime() { return scheduleTime; }
    public void setScheduleTime(LocalDateTime scheduleTime) { this.scheduleTime = scheduleTime; }

    
    @Override
    public String toString() {
        return "ScheduledNotificationDTO{" +
                "username='" + username + '\'' +
                ", message='" + message + '\'' +
                ", scheduleTime=" + scheduleTime +
                '}';
    }

}
