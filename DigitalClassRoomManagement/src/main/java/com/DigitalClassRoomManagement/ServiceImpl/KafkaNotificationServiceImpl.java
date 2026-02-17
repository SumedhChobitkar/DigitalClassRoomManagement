package com.DigitalClassRoomManagement.ServiceImpl;

import com.DigitalClassRoomManagement.Dto.KafkaNotificationDto;
import com.DigitalClassRoomManagement.Entity.KafkaNotification;
import com.DigitalClassRoomManagement.Entity.Notification;
import com.DigitalClassRoomManagement.Repository.KafkaNotificationRepository;
import com.DigitalClassRoomManagement.Service.KafkaNotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class KafkaNotificationServiceImpl implements KafkaNotificationService {

    private final KafkaNotificationRepository notificationRepository;
    private final SimpMessagingTemplate messagingTemplate;

    @KafkaListener(
            topics = { "notifications", "notification-topic" },
            groupId = "notification-group",
            containerFactory = "notificationKafkaListenerFactory"
    )
    public void consume(KafkaNotificationDto event) {

        if (event == null) return;

        System.out.println(" Kafka notification received: " + event);

        KafkaNotification notification = KafkaNotification.builder()
                .userId(event.getUserId())   // nullable for holiday
                .title(event.getTitle())
                .message(event.getMessage())
                .source(event.getSource())
                .createdAt(LocalDateTime.now())
                .build();

        notificationRepository.save(notification);

        //  Send WebSocket ONLY if user-specific
        if (event.getUserId() != null && event.getReceiverRole() != null) {

            String destination =
                    "/topic/notifications/" +
                            event.getReceiverRole().toLowerCase() +
                            "/" +
                            event.getUserId();

            messagingTemplate.convertAndSend(destination, event);
        }

        System.out.println(" Notification saved in DB");
    }

    // Fetch yesterday + today notifications
    @Override
    public List<KafkaNotification> getNotificationsForYesterdayAndToday(String userId) {

        LocalDate today = LocalDate.now();
        LocalDate yesterday = today.minusDays(1);

        LocalDateTime startDate = yesterday.atStartOfDay();
        LocalDateTime endDate = today.plusDays(1).atStartOfDay();

        return notificationRepository
                .findByUserIdAndCreatedAtBetweenOrderByCreatedAtDesc(
                        userId,
                        startDate,
                        endDate
                );
    }

    @Override
    @Scheduled(cron = "0 0 2 * * ?")
    @Transactional
    public void deleteOldNotifications() {

        LocalDateTime cutoffDate = LocalDateTime.now().minusDays(7);

        int deletedCount = notificationRepository.deleteOlderThan(cutoffDate);

        log.info(" Deleted {} notifications older than 7 days", deletedCount);
    }
}
