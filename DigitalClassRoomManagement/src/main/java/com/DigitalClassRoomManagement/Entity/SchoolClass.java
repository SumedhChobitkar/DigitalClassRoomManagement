package com.DigitalClassRoomManagement.Entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
@Entity
@Table(name = "school_classes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SchoolClass {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long classId;

    @NotBlank(message = "Class name is required")
    @Size(min = 2, max = 50)
    @Column(nullable = false, unique = true, length = 50)
    private String className;

    @NotBlank(message = "Description is required")
    @Size(max = 200)
    @Column(nullable = false, length = 200)
    private String description;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    //Ignored Section mapping
    @OneToMany(mappedBy = "schoolClass", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Section> sections = new ArrayList<>();

    // Reverse mapping
    @ManyToMany(mappedBy = "assignedClass", fetch = FetchType.EAGER)
    @JsonIgnore
    private List<Teacher> teachers = new ArrayList<>();

    @OneToMany(mappedBy = "schoolClass")
    private List<Timetable> timetables;

    // student mapping
    @OneToMany(mappedBy = "schoolClass", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Student> students = new ArrayList<>();

    // ---Sessions mapping (one class can have many sessions)---
    @OneToMany(mappedBy = "schoolClass", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Session> sessions = new ArrayList<>();

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
