package com.DigitalClassRoomManagement.ServiceImpl;

import com.DigitalClassRoomManagement.Dto.ChatMessageDto;
import com.DigitalClassRoomManagement.Dto.ChatMessageResponseDto;
import com.DigitalClassRoomManagement.Entity.ChatMessage;
import com.DigitalClassRoomManagement.Entity.Parent;
import com.DigitalClassRoomManagement.Entity.Student;
import com.DigitalClassRoomManagement.Entity.Teacher;
import com.DigitalClassRoomManagement.Mapper.ChatMapper;
import com.DigitalClassRoomManagement.Repository.ChatMessageRepository;
import com.DigitalClassRoomManagement.Repository.ParentRepository;
import com.DigitalClassRoomManagement.Repository.StudentRepository;
import com.DigitalClassRoomManagement.Repository.TeacherRepository;
import com.DigitalClassRoomManagement.Service.ChatService;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
public class ChatServiceImpl implements ChatService {

    private final StudentRepository studentRepo;
    private final ParentRepository parentRepo;
    private final TeacherRepository teacherRepo;
    private final ChatMessageRepository chatRepo;

    public ChatServiceImpl(StudentRepository studentRepo,
                           ParentRepository parentRepo,
                           TeacherRepository teacherRepo,
                           ChatMessageRepository chatRepo) {
        this.studentRepo = studentRepo;
        this.parentRepo = parentRepo;
        this.teacherRepo = teacherRepo;
        this.chatRepo = chatRepo;
    }

    @Override
    public ChatMessage save(ChatMessageDto dto) {
        try {
            log.info(" Saving chat message | sender={}, teacherId={}, studentId={}, parentId={}",
                    dto.getSender(), dto.getTeacherId(), dto.getStudentRegId(), dto.getParentId());

            if (dto.getTeacherId() == null) {
                throw new IllegalArgumentException("TeacherId is required");
            }

            boolean isStudentChat = dto.getStudentRegId() != null;
            boolean isParentChat = dto.getParentId() != null;

            if (isStudentChat == isParentChat) {
                throw new IllegalArgumentException(
                        "Message must be either Student-Teacher OR Parent-Teacher"
                );
            }

            Teacher teacher = teacherRepo.findById(dto.getTeacherId())
                    .orElseThrow(() -> new RuntimeException("Teacher not found"));

            ChatMessage msg = new ChatMessage();
            msg.setTeacher(teacher);
            msg.setSender(dto.getSender());
            msg.setMessage(dto.getMessage());
            msg.setTimestamp(LocalDateTime.now());

            if (isStudentChat) {
                Student student = studentRepo.findByStudentRegId(dto.getStudentRegId())
                        .orElseThrow(() -> new RuntimeException("Student not found"));
                msg.setStudent(student);
                log.debug(" Student attached: {}", student.getStudentRegId());
            }

            if (isParentChat) {
                Parent parent = parentRepo.findById(dto.getParentId())
                        .orElseThrow(() -> new RuntimeException("Parent not found"));
                msg.setParent(parent);
                log.debug(" Parent attached: {}", parent.getParentId());
            }

            ChatMessage saved = chatRepo.save(msg);
            log.info(" Chat message saved with ID {}", saved.getMessageId());

            return saved;

        } catch (Exception e) {
            log.error(" Failed to save chat message", e);
            throw e;
        }
    }



    @Override
    public List<ChatMessage> getLastWeekMessagesForTeacher(Long teacherId, String sender) {
        try {
            log.info(" Fetching last week {} messages for teacher {}", sender, teacherId);

            if (!sender.equals("STUDENT") && !sender.equals("PARENT")) {
                throw new IllegalArgumentException("Invalid sender type");
            }

            return chatRepo.findByTeacher_IdAndSenderAndTimestampAfter(
                    teacherId,
                    sender,
                    LocalDateTime.now().minusDays(7)
            );

        } catch (Exception e) {
            log.error(" Failed to fetch last week messages", e);
            throw e;
        }
    }



    @Override
    public void updateMessage(Long messageId, String newMessage) {
        try {
            log.info(" Updating message {}", messageId);

            ChatMessage msg = chatRepo.findById(messageId)
                    .orElseThrow(() -> new RuntimeException("Message not found"));

            if (Duration.between(msg.getTimestamp(), LocalDateTime.now()).toMinutes() > 10) {
                throw new RuntimeException("Update window expired");
            }

            msg.setMessage(newMessage);
            chatRepo.save(msg);

            log.info(" Message {} updated", messageId);

        } catch (Exception e) {
            log.error(" Failed to update message {}", messageId, e);
            throw e;
        }
    }


    @Override
    @Scheduled(cron = "0 0 2 * * ?")
    @Transactional
    public void deleteOldChatMessages() {
        try {
            LocalDateTime cutoff = LocalDateTime.now().minusDays(7);
            log.info(" Deleting chat messages older than {}", cutoff);

            chatRepo.deleteMessagesOlderThan(cutoff);

            log.info(" Old chat messages cleanup completed");

        } catch (Exception e) {
            log.error(" Chat cleanup job failed", e);
        }
    }



    @Override
    public List<ChatMessageResponseDto> getStudentTeacherChats(Long teacherId, Long studentRegId) {
        try {
            log.info("Fetching student-teacher chat | teacher={}, student={}", teacherId, studentRegId);

            return chatRepo
                    .findByTeacher_IdAndStudent_StudentRegIdOrderByTimestampAsc(teacherId, studentRegId)
                    .stream()
                    .map(ChatMapper::toDto)
                    .toList();

        } catch (Exception e) {
            log.error(" Failed to fetch student-teacher chats", e);
            throw e;
        }
    }

    @Override
    public List<ChatMessageResponseDto> getParentTeacherChats(Long teacherId, Long parentId) {
        try {
            log.info(" Fetching parent-teacher chat | teacher={}, parent={}", teacherId, parentId);

            return chatRepo
                    .findByTeacher_IdAndParent_ParentIdOrderByTimestampAsc(teacherId, parentId)
                    .stream()
                    .map(ChatMapper::toDto)
                    .toList();

        } catch (Exception e) {
            log.error(" Failed to fetch parent-teacher chats", e);
            throw e;
        }
    }

    @Override
    public List<ChatMessageResponseDto> getMessagesForTeacher(Long teacherId) {
        try {
            log.info(" Fetching all messages for teacher {}", teacherId);

            return chatRepo.findByTeacher_IdOrderByTimestampAsc(teacherId)
                    .stream()
                    .map(ChatMapper::toDto)
                    .toList();

        } catch (Exception e) {
            log.error(" Failed to fetch teacher messages", e);
            throw e;
        }
    }



    @Override
    @Transactional
    public void markAsReadForTeacher(Long teacherId) {
        try {
            log.info(" Marking messages as read for teacher {}", teacherId);
            chatRepo.markTeacherMessagesAsRead(teacherId, LocalDateTime.now());
        } catch (Exception e) {
            log.error(" Failed to mark teacher messages as read", e);
            throw e;
        }
    }

    @Override
    @Transactional
    public void markAsReadForStudent(Long teacherId, Long studentRegId) {
        try {
            log.info(" Marking messages as read for student {} (teacher {})", studentRegId, teacherId);
            chatRepo.markStudentMessagesAsRead(teacherId, studentRegId, LocalDateTime.now());
        } catch (Exception e) {
            log.error(" Failed to mark student messages as read", e);
            throw e;
        }
    }

    @Override
    @Transactional
    public void markAsReadForParent(Long teacherId, Long parentId) {
        try {
            log.info(" Marking messages as read for parent {} (teacher {})", parentId, teacherId);
            chatRepo.markParentMessagesAsRead(teacherId, parentId, LocalDateTime.now());
        } catch (Exception e) {
            log.error(" Failed to mark parent messages as read", e);
            throw e;
        }
    }
}
