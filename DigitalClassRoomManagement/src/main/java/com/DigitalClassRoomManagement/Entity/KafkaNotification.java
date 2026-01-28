package com.DigitalClassRoomManagement.Entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "kafka_Notifications")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class KafkaNotification {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String notificationId;

    private String userId;

    private String title;

    private String message;

    private String source;

    private LocalDateTime createdAt;
}

