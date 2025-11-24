package com.DigitalClassRoomManagement.Entity;

import com.DigitalClassRoomManagement.Enum.Action;
import com.DigitalClassRoomManagement.Enum.Module;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuditLog {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long auditLogId;

    private Long userId;

    private String username;

    @Enumerated(EnumType.STRING)
    private Action action;  // Enum: CREATE, UPDATE, DELETE, LOGIN, LOGOUT etc.

    @Enumerated(EnumType.STRING)
    private Module module;  // Enum: HOMEWORK, STUDENT, TEACHER etc.

    private LocalDateTime time;
}
