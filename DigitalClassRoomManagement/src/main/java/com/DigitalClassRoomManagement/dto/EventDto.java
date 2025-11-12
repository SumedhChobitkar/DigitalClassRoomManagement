package com.DigitalClassRoomManagement.Dto;


import lombok.*;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EventDto {
    private Long id;
    private String eventName;
    private String description;
    private LocalDate eventDate;
}
