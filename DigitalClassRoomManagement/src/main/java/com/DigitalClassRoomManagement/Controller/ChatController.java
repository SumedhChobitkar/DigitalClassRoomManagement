package com.DigitalClassRoomManagement.Controller;

import com.DigitalClassRoomManagement.Dto.ChatMessageDto;
import com.DigitalClassRoomManagement.Dto.ChatMessageResponseDto;
import com.DigitalClassRoomManagement.Dto.KafkaNotificationDto;
import com.DigitalClassRoomManagement.Entity.ChatMessage;
import com.DigitalClassRoomManagement.Mapper.ChatMapper;
import com.DigitalClassRoomManagement.Service.ChatService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/chat")
public class ChatController {

    private final ChatService chatService;
    private final SimpMessagingTemplate messagingTemplate;

    @Autowired
    private KafkaTemplate<String, KafkaNotificationDto> kafkaTemplate;


    public ChatController(ChatService chatService,
                          SimpMessagingTemplate messagingTemplate) {
        this.chatService = chatService;
        this.messagingTemplate = messagingTemplate;
    }


    @PreAuthorize("hasAnyRole('STUDENT','TEACHER','PARENT')")
@MessageMapping("/chat/send")
public void send(ChatMessageDto dto) {

    try {
        log.info("Incoming chat message: sender={}, teacherId={}, studentId={}, parentId={}",
                dto.getSender(), dto.getTeacherId(), dto.getStudentRegId(), dto.getParentId());

        // 1️⃣ Save chat message
        ChatMessage savedMessage = chatService.save(dto);

        ChatMessageResponseDto response = new ChatMessageResponseDto(
                savedMessage.getMessageId(),
                savedMessage.getStudent() != null ? savedMessage.getStudent().getStudentRegId() : null,
                savedMessage.getParent() != null ? savedMessage.getParent().getParentId() : null,
                savedMessage.getTeacher().getId(),
                savedMessage.getSender(),
                savedMessage.getMessage(),
                savedMessage.getTimestamp()
        );

        log.warn("🧪 DB sender value = {}", savedMessage.getSender());

        // 2️⃣ Capture sender role ONCE (immutable)
        final String senderRole = savedMessage.getSender().toUpperCase();

        // ================= CHAT DELIVERY =================

        if ("STUDENT".equals(senderRole) || "PARENT".equals(senderRole)) {
            // Student / Parent → Teacher
            messagingTemplate.convertAndSend(
                    "/topic/chat/teacher/" + savedMessage.getTeacher().getId(),
                    response
            );

        } else if ("TEACHER".equals(senderRole)) {

            // Teacher → Student
            if (savedMessage.getStudent() != null) {
                messagingTemplate.convertAndSend(
                        "/topic/chat/student/" + savedMessage.getStudent().getStudentRegId(),
                        response
                );
            }

            // Teacher → Parent
            if (savedMessage.getParent() != null) {
                messagingTemplate.convertAndSend(
                        "/topic/chat/parent/" + savedMessage.getParent().getParentId(),
                        response
                );
            }
        }

        // ================= NOTIFICATION LOGIC =================

        String senderId;
        String receiverId;
        String receiverRole;

        // ---- Sender identity ----
        switch (senderRole) {
            case "TEACHER" -> senderId = savedMessage.getTeacher().getId().toString();
            case "STUDENT" -> senderId = savedMessage.getStudent().getStudentRegId().toString();
            case "PARENT"  -> senderId = savedMessage.getParent().getParentId().toString();
            default -> {
                log.warn("Unknown sender role {}", senderRole);
                return;
            }
        }

        // ---- Receiver identity ----
        if ("TEACHER".equals(senderRole)) {

            if (savedMessage.getStudent() != null) {
                receiverId = savedMessage.getStudent().getStudentRegId().toString();
                receiverRole = "STUDENT";
            } else {
                receiverId = savedMessage.getParent().getParentId().toString();
                receiverRole = "PARENT";
            }

        } else {
            receiverId = savedMessage.getTeacher().getId().toString();
            receiverRole = "TEACHER";
        }

        // ---- Role-aware self-notification guard ----
        String senderIdentity   = senderRole + ":" + senderId;
        String receiverIdentity = receiverRole + ":" + receiverId;

        if (senderIdentity.equals(receiverIdentity)) {
            log.info("🚫 Notification skipped (same logical user)");
            return;
        }

        // ---- Build notification DTO (SAFE via setters) ----
        KafkaNotificationDto notificationEvent = new KafkaNotificationDto();
        notificationEvent.setUserId(receiverId);                 // RECEIVER ID
        notificationEvent.setReceiverRole(receiverRole);          // RECEIVER ROLE
        notificationEvent.setTitle("New Message");
        notificationEvent.setMessage("You have a new message from " + senderRole);
        notificationEvent.setSource("CHAT");
        notificationEvent.setSenderId(senderId);                  // SENDER ID

        // ---- Send to Kafka ----
        kafkaTemplate.send("notifications", notificationEvent);

        log.info("📤 Notification sent to Kafka | {} → {}",
                senderIdentity, receiverIdentity);

    } catch (Exception e) {
        log.error("Error while sending chat message", e);
    }
}

    @GetMapping("/student-messages")
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<List<ChatMessageResponseDto>> getStudentMessages(
            @RequestParam Long teacherId
    ) {
        try {
            log.info(" Teacher {} fetching student messages", teacherId);
            return ResponseEntity.ok(
                    chatService.getLastWeekMessagesForTeacher(teacherId, "STUDENT")
                            .stream().map(ChatMapper::toDto).toList()
            );
        } catch (Exception e) {
            log.error(" Failed to fetch student messages", e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/parent-messages")
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<List<ChatMessageResponseDto>> getParentMessages(
            @RequestParam Long teacherId
    ) {
        try {
            log.info(" Teacher {} fetching parent messages", teacherId);
            return ResponseEntity.ok(
                    chatService.getLastWeekMessagesForTeacher(teacherId, "PARENT")
                            .stream().map(ChatMapper::toDto).toList()
            );
        } catch (Exception e) {
            log.error(" Failed to fetch parent messages", e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/student-teacher")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<List<ChatMessageResponseDto>> getStudentTeacherChats(
            @RequestParam Long teacherId,
            @RequestParam Long studentRegId
    ) {
        try {
            log.info(" Student {} fetching chat with teacher {}", studentRegId, teacherId);
            return ResponseEntity.ok(
                    chatService.getStudentTeacherChats(teacherId, studentRegId)
            );
        } catch (Exception e) {
            log.error(" Failed to fetch student-teacher chats", e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/parent-teacher")
    @PreAuthorize("hasRole('PARENT')")
    public ResponseEntity<List<ChatMessageResponseDto>> getParentTeacherChats(
            @RequestParam Long teacherId,
            @RequestParam Long parentId
    ) {
        try {
            log.info(" Parent {} fetching chat with teacher {}", parentId, teacherId);
            return ResponseEntity.ok(
                    chatService.getParentTeacherChats(teacherId, parentId)
            );
        } catch (Exception e) {
            log.error(" Failed to fetch parent-teacher chats", e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/teacher/messages")
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<List<ChatMessageResponseDto>> getTeacherMessages(
            @RequestParam Long teacherId
    ) {
        try {
            log.info(" Teacher {} fetching all messages", teacherId);
            return ResponseEntity.ok(chatService.getMessagesForTeacher(teacherId));
        } catch (Exception e) {
            log.error(" Failed to fetch teacher messages", e);
            return ResponseEntity.internalServerError().build();
        }
    }


    @PostMapping("/teacher/open")
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<Void> teacherOpened(@RequestParam Long teacherId) {
        try {
            log.info(" Teacher {} opened chat", teacherId);
            chatService.markAsReadForTeacher(teacherId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            log.error(" Error marking teacher messages as read", e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping("/student/open")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<Void> studentOpened(
            @RequestParam Long teacherId,
            @RequestParam Long studentRegId
    ) {
        try {
            log.info(" Student {} opened chat with teacher {}", studentRegId, teacherId);
            chatService.markAsReadForStudent(teacherId, studentRegId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            log.error(" Error marking student messages as read", e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping("/parent/open")
    @PreAuthorize("hasRole('PARENT')")
    public ResponseEntity<Void> parentOpened(
            @RequestParam Long teacherId,
            @RequestParam Long parentId
    ) {
        try {
            log.info(" Parent {} opened chat with teacher {}", parentId, teacherId);
            chatService.markAsReadForParent(teacherId, parentId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            log.error(" Error marking parent messages as read", e);
            return ResponseEntity.internalServerError().build();
        }
    }
}
