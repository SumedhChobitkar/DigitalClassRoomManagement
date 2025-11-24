package com.DigitalClassRoomManagement.Entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Table(name = "homeworks")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class homework {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long homeworkId;

    private String title;

    private String description;

    private LocalDate assignedDate;

    private LocalDate dueDate;
}
