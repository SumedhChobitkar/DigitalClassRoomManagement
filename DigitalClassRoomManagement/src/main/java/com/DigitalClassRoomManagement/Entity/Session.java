package com.DigitalClassRoomManagement.Entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Session")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Session {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)

    //@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "session_seq")
    //@SequenceGenerator(
          //  name = "session_seq",
          //  sequenceName = "session_seq",
          //  allocationSize = 1
  //  )

    private Long sessionId;

    // One-to-one with Timetable (owner side)
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "timetableId", nullable = false)
    private Timetable timetable;

    // Many sessions can belong to one class
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "classId", nullable = false)
    private SchoolClass schoolClass;

    // Many sessions can belong to one section
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "sectionId", nullable = false)
    private Section section;

    // Many sessions taught by one teacher
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "teacherId", nullable = false)
    private Teacher teacher;

    @FutureOrPresent(message = "Only Present or Future date is allowed")
    @Column(nullable = false)
    private LocalDate date;

    @Column(nullable = false)
    private LocalDateTime startTime;

    @Column(nullable = false)
    private LocalDateTime endTime;

    @NotBlank
    @Size(min = 2, max = 50)
    @Column(nullable = false)
    private String topic;

    @Size(max = 300)
    private String description;

    private String joinLink;
    private String googleEventId;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @PrePersist
    public void onCreate(){
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    public void onUpdate(){
        this.updatedAt = LocalDateTime.now();
    }

    //HELPER
    public boolean isPastSession() {
        return this.date.isBefore(LocalDate.now());
    }

}