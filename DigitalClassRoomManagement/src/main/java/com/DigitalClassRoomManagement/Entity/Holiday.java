package com.DigitalClassRoomManagement.Entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Holiday {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String holidayName;
    private LocalDate holidayDate;
    private String description;

    @ManyToOne
    @JoinColumn(name = "calendar_id")
    @JsonBackReference(value = "calendar-holiday")
    private AcademicCalendar calendar;

}
