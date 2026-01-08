package com.DigitalClassRoomManagement.Dto;

import com.DigitalClassRoomManagement.Entity.SchoolClass;
import com.DigitalClassRoomManagement.Entity.Teacher;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SubjectDto {

     private Long subjectId;
     private String subjectName;
     private String subjectCode;
     private Long classId;
     private String className;
     private Long id;
     private String description;
     private Boolean isActive = true;
     private BigDecimal maxMarks;
     private LocalDateTime createdAt;
     private LocalDateTime updatedAt;
}