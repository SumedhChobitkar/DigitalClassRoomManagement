package com.DigitalClassRoomManagement.Entity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String eventName;
    private String description;
    private LocalDate eventDate;

    @ManyToOne
    @JoinColumn(name = "calendar_id")
    private AcademicCalendar calendar;
}

