package com.DigitalClassRoomManagement.Repository;



import com.DigitalClassRoomManagement.Entity.KafkaNotification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface KafkaNotificationRepository
        extends JpaRepository<KafkaNotification, String> {

    List<KafkaNotification> findByUserIdAndCreatedAtBetweenOrderByCreatedAtDesc(
            String userId,
            LocalDateTime startDate,
            LocalDateTime endDate
    );
}
