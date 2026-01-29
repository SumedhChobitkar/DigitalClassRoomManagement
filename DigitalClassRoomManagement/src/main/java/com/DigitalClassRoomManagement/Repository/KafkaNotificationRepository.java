package com.DigitalClassRoomManagement.Repository;



import com.DigitalClassRoomManagement.Entity.KafkaNotification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

public interface KafkaNotificationRepository
        extends JpaRepository<KafkaNotification, String> {

    List<KafkaNotification> findByUserIdAndCreatedAtBetweenOrderByCreatedAtDesc(
            String userId,
            LocalDateTime startDate,
            LocalDateTime endDate
    );

    @Modifying
    @Transactional
    @Query("DELETE FROM KafkaNotification k WHERE k.createdAt < :cutoffDate")
    int deleteOlderThan(@Param("cutoffDate") LocalDateTime cutoffDate);
}
