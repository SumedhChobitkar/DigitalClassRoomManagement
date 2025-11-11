package com.DigitalClassRoomManagement.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@Builder
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name="Teacher")
public class Teacher {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String qualification;
    private Integer experienceYears;
    private String gender;
    private String dateOfBirth;
    private String assign_class;
    private String assign_section;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User user;


//    @ManyToMany
//    @JoinTable(
//            name = "teacher_class",
//            joinColumns = @JoinColumn(name = "teacher_id"),
//            inverseJoinColumns = @JoinColumn(name = "class_id")
//    )
//    @Builder.Default
//    private List<SchoolClass> assignedClass = new ArrayList<>();
//
//    @ManyToMany
//    @JoinTable(
//            name = "teacher_section",
//            joinColumns = @JoinColumn(name = "teacher_id"),
//            inverseJoinColumns = @JoinColumn(name = "section_id")
//    )
//    @Builder.Default
//    private List<Section> assignedSection = new ArrayList<>();
//
//    @ManyToMany
//    @JoinTable(
//            name = "teacher_student",
//            joinColumns = @JoinColumn(name = "teacher_id"),
//            inverseJoinColumns = @JoinColumn(name = "student_reg_id")
//    )
//    @Builder.Default
//    private List<Student> student = new ArrayList<>();
//
//
//    @Column(name = "assigned_at")
//    private LocalDateTime assignedAt;
//
//    private boolean assignedAsClassTeacher;
//
//    @OneToOne(fetch=FetchType.LAZY)
//    @JoinColumn(name="classTeacherId")
//    private ClassTeacher classTeacher;
//
//    @OneToMany(mappedBy = "teacher", cascade = CascadeType.ALL, orphanRemoval = true)
//    private List<PTM> ptms;
}
