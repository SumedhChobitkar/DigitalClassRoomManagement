package com.DigitalClassRoomManagement.Entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.Getter;

@Entity
@Data
public class AssignTeacherRequest {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "class_id")
    private SchoolClass schoolClass;

    @ManyToOne
    @JoinColumn(name = "sectionId")
    private Section sectionId;


    @ManyToOne
    @JoinColumn(name = "teacher_id")
    private Teacher teacher;

}
