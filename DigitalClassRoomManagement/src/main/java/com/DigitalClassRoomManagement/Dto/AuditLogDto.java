package com.DigitalClassRoomManagement.Dto;

import com.DigitalClassRoomManagement.Enum.Action;
import com.DigitalClassRoomManagement.Enum.Module;
import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuditLogDto {
    private Long userId;
    private String username;
    private Action action;
    private Module module;
    private LocalDateTime time;
}
