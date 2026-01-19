package com.DigitalClassRoomManagement.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
@Entity
@Table(name = "sections")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Section {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long sectionId;  // this will map to 'section_id' in join table

    @NotBlank(message = "Section name is required")
    @Size(min = 1, max = 50, message = "Section name must be between 1 and 50 characters")
    @Column(nullable = false, length = 50)
    private String sectionName;

    @NotNull(message = "Capacity is required")
    @Min(value = 1, message = "Capacity must be at least 1")
    @Max(value = 200, message = "Capacity cannot exceed 200 students")
    private Integer capacity;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @ManyToMany(mappedBy = "assignedSections", cascade = {CascadeType.PERSIST, CascadeType.MERGE})

    //@ManyToMany(mappedBy = "assignedSections")
    @JsonIgnore
    private List<Teacher> teachers = new ArrayList<>();

    //private String schoolClass;
    @ManyToOne
    @JoinColumn(name = "class_id", nullable = false)
    @JsonIgnore
    private SchoolClass schoolClass;

    //student mapping
    @OneToMany(mappedBy = "section")
    @JsonIgnore
    private List<Student> students = new ArrayList<>();

    // ---Sessions mapping (one section can have many sessions)---
    @OneToMany(mappedBy = "section", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Session> sessions = new ArrayList<>();

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
