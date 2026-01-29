package com.DigitalClassRoomManagement.Service;

import com.DigitalClassRoomManagement.Dto.KafkaNotificationDto;
import com.DigitalClassRoomManagement.Entity.KafkaNotification;

import java.util.List;

public interface KafkaNotificationService {

    // Consume notification from Kafka
    void consume(KafkaNotificationDto event);

    // Get notifications for yesterday and today for a user
    List<KafkaNotification> getNotificationsForYesterdayAndToday(String userId);
    void deleteOldNotifications();
}
