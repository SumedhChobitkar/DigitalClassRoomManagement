package com.DigitalClassRoomManagement.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import java.sql.Blob;
import java.time.LocalDate;

@Entity
@Table(name = "study_materials")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StudyMaterial {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long materialId;

    private String title;

    private String type; // Notes, Worksheet, LessonPlan
    @Lob
    @JsonIgnore
    private Blob fileUrl;

    private LocalDate uploadDate;
}
