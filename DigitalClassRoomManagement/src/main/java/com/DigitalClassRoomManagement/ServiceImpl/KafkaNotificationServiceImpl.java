package com.DigitalClassRoomManagement.ServiceImpl;

import com.DigitalClassRoomManagement.Dto.KafkaNotificationDto;
import com.DigitalClassRoomManagement.Entity.KafkaNotification;
import com.DigitalClassRoomManagement.Repository.KafkaNotificationRepository;
import com.DigitalClassRoomManagement.Service.KafkaNotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class KafkaNotificationServiceImpl implements KafkaNotificationService {

    private final KafkaNotificationRepository notificationRepository;
    private final SimpMessagingTemplate messagingTemplate;

    // Kafka Consumer
    @Override
    @KafkaListener(
            topics = "notifications",
            containerFactory = "notificationKafkaListenerFactory"
    )
    public void consume(KafkaNotificationDto event) {

        if (event == null || event.getUserId() == null) {
            return;
        }



        System.out.println(" Kafka notification received: " + event);

        KafkaNotification notification = new KafkaNotification(
                null,
                event.getUserId(),   // RECEIVER
                event.getTitle(),
                event.getMessage(),
                event.getSource(),
                LocalDateTime.now()
        );

        notificationRepository.save(notification);

        String destination =
                "/topic/notifications/" +
                        event.getReceiverRole().toLowerCase() +
                        "/" +
                        event.getUserId();

        messagingTemplate.convertAndSend(destination, event);

    }


    // Fetch yesterday + today notifications
    @Override
    public List<KafkaNotification> getNotificationsForYesterdayAndToday(String userId) {

        LocalDate today = LocalDate.now();
        LocalDate yesterday = today.minusDays(1);

        LocalDateTime startDate = yesterday.atStartOfDay(); // yesterday 00:00
        LocalDateTime endDate = today.plusDays(1).atStartOfDay(); // tomorrow 00:00

        return notificationRepository
                .findByUserIdAndCreatedAtBetweenOrderByCreatedAtDesc(
                        userId,
                        startDate,
                        endDate
                );
    }
}
