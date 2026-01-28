package com.DigitalClassRoomManagement.Dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChatMessageDto {

    private Long studentRegId;   // optional
    private Long parentId;    // optional
    private Long teacherId;   // required

    private String sender;    // STUDENT / PARENT / TEACHER
    private String message;
    private boolean read;
}
