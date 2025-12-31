package com.DigitalClassRoomManagement.Entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import com.DigitalClassRoomManagement.Enum.DayOfWeek;
//import java.time.DayOfWeek;


import java.time.LocalDate;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Builder
@JsonIgnoreProperties({"schoolClass", "section", "subject", "teacher"})
public class Timetable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long timetableId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "classId", nullable = false)
    private SchoolClass schoolClass;
    //private String schoolClass;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sectionId", nullable = false)
    private Section section;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subjectId", nullable = false)
    private Subject subject;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "teacherId", nullable = false)
    private Teacher teacher;

//  @NotNull(message = "Day of week is required")
    @Enumerated(EnumType.STRING)
    private DayOfWeek dayOfWeek;

    @FutureOrPresent(message = "Only Present or Future date is allowed")
    @Column(nullable = false)
    private LocalDate date;

    @Column(nullable = false)
    private LocalDateTime startTime;

    @Column(nullable = false)
    private LocalDateTime endTime;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    //--- for Session---
    // ---Reverse one-to-one link to Session (mappedBy = "timetable")
    @OneToOne(mappedBy = "timetable", fetch = FetchType.LAZY)
    private Session session;

    @PrePersist
    public void onCreated(){
        this.createdAt = LocalDateTime.now();
    }

    @PreUpdate
    public void onUpdated(){
        this.updatedAt = LocalDateTime.now();
    }
}

