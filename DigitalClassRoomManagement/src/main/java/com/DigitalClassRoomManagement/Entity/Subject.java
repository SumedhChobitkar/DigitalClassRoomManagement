package com.DigitalClassRoomManagement.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Subject {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long subjectId;

    private String subjectName;

    @NotBlank
    @Column(unique = true)
    private String subjectCode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "classId", nullable = false)
    private SchoolClass schoolClass;
//    @NotBlank
//    private String schoolClass;

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "id")
    @JsonIgnore
    private Teacher teacher;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Builder.Default
    private Boolean isActive = true;
    @Column(nullable = false)
    private BigDecimal maxMarks;
    @CreationTimestamp
    private LocalDateTime createdAt;
    @UpdateTimestamp
    private LocalDateTime updatedAt;
}