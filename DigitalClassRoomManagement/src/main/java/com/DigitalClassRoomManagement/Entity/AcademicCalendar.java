package com.DigitalClassRoomManagement.Entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AcademicCalendar {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String academicYear;
    private LocalDate startDate;
    private LocalDate endDate;
    private String createdBy;

    @OneToMany(
            mappedBy = "calendar",
            cascade = {CascadeType.PERSIST, CascadeType.MERGE},
            orphanRemoval = true,
            fetch = FetchType.EAGER
    )
    @JsonManagedReference(value = "calendar-holiday")
    private List<Holiday> holidays;

    @OneToMany(
            mappedBy = "calendar",
            cascade = {CascadeType.PERSIST, CascadeType.MERGE},
            orphanRemoval = true,
            fetch = FetchType.EAGER
    )
    @JsonManagedReference(value = "calendar-event")
    private List<Event> events;


}
