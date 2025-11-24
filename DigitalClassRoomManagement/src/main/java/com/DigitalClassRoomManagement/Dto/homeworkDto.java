package com.DigitalClassRoomManagement.Dto;

import lombok.*;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class homeworkDto {

    private String title;
    private String description;
    private LocalDate assignedDate;
    private LocalDate dueDate;
}
