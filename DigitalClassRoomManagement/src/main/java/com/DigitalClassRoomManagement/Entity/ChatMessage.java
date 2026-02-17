package com.DigitalClassRoomManagement.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "chat_messages")
@Getter
@Setter
public class ChatMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long messageId;

    @ManyToOne
    @JoinColumn(name = "student_reg_id", nullable = true)
    private Student student;     // nullable

    @ManyToOne
    @JoinColumn(name = "parent_id", nullable = true)
    private Parent parent;       // nullable

    @ManyToOne
    @JoinColumn(name = "teacher_id", nullable = false)
    private Teacher teacher;

    private String sender;   // STUDENT / PARENT / TEACHER
    private String message;
    private LocalDateTime timestamp;
    private LocalDateTime readAt;
    @Column(name = "is_read")
    private boolean isRead = false;

}
