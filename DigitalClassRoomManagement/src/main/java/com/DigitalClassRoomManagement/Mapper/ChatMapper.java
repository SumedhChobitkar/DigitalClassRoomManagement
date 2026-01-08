package com.DigitalClassRoomManagement.Mapper;

import com.DigitalClassRoomManagement.Dto.ChatMessageResponseDto;
import com.DigitalClassRoomManagement.Entity.ChatMessage;

public class ChatMapper {

    private ChatMapper() {
        // prevent instantiation
    }

    public static ChatMessageResponseDto toDto(ChatMessage msg) {

        return new ChatMessageResponseDto(
                msg.getMessageId(),
                msg.getStudent() != null ? msg.getStudent().getStudentRegId() : null,
                msg.getParent() != null ? msg.getParent().getParentId() : null,
                msg.getTeacher().getId(),
                msg.getSender(),
                msg.getMessage(),
                msg.getTimestamp(),
                msg.getReadAt()
        );
    }
}
