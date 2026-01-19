package com.DigitalClassRoomManagement.Service;


import com.DigitalClassRoomManagement.Dto.ChatMessageDto;
import com.DigitalClassRoomManagement.Dto.ChatMessageResponseDto;
import com.DigitalClassRoomManagement.Entity.ChatMessage;


import java.util.List;

public interface ChatService {

    ChatMessage save(ChatMessageDto dto);
    List<ChatMessage> getLastWeekMessagesForTeacher(
            Long teacherId,
            String sender
    );

    void updateMessage(Long messageId, String newMessage);
    void deleteOldChatMessages();


    List<ChatMessageResponseDto> getStudentTeacherChats(
            Long teacherId,
            Long studentRegId
    );

    List<ChatMessageResponseDto> getParentTeacherChats(
            Long teacherId,
            Long parentId
    );

    List<ChatMessageResponseDto> getMessagesForTeacher(Long teacherId);

    void markAsReadForTeacher(Long teacherId);
    void markAsReadForStudent(Long teacherId, Long studentRegId);
    void markAsReadForParent(Long teacherId, Long parentId);

}
