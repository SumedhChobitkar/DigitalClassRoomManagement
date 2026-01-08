package com.DigitalClassRoomManagement.Dto;

import lombok.*;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatMessageResponseDto {

    private Long messageId;
    private Long studentRegId;
    private Long parentId;
    private Long teacherId;
    private String sender;
    private String message;
    private LocalDateTime timestamp;
    private LocalDateTime readAt;

    public ChatMessageResponseDto(
            Long messageId,
            Long studentRegId,
            Long parentId,
            Long teacherId,
            String sender,
            String message,
            LocalDateTime timestamp
    ) {
        this.messageId = messageId;
        this.studentRegId = studentRegId;
        this.parentId = parentId;
        this.teacherId = teacherId;
        this.sender = sender;
        this.message = message;
        this.timestamp = timestamp;
    }
}
