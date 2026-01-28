package com.DigitalClassRoomManagement.Dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class KafkaNotificationDto {
    private String notificationId;
    private String userId;
    private String title;
    private String message;
    private String source;
    private String senderId;
    private String receiverRole;
}
