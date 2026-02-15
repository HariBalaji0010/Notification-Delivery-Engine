package com.notificationDeliveryEngine.repository;




import org.springframework.data.jpa.repository.JpaRepository;

import com.notificationDeliveryEngine.entity.DeliveryHistory;

import java.util.List;
import java.util.UUID;

public interface DeliveryHistoryRepository extends JpaRepository<DeliveryHistory, UUID> {
    List<DeliveryHistory> findByNotificationIdOrderByUpdatedAtDesc(UUID notificationId);
}