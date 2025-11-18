package com.DigitalClassRoomManagement.Dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SchoolClassResponseDto {

    private Long classId;
    private String className;
    private String description;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Only IDs to avoid lazy issues
    private List<Long> teacherIds;
}
