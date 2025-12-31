package com.DigitalClassRoomManagement.Entity;

import com.DigitalClassRoomManagement.Enum.TeacherStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.micrometer.common.lang.Nullable;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name="Teacher", uniqueConstraints = @UniqueConstraint(columnNames = "email"))
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
    private String adminMailId;
    private String gender;
    private String dateOfBirth;
    @OneToOne
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User user;

    @Enumerated(EnumType.STRING)
    private TeacherStatus status;

    @ManyToMany
    @JoinTable(
            name = "teacher_section",
            joinColumns = @JoinColumn(name = "teacher_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "section_id", referencedColumnName = "sectionId")
    )
    @JsonIgnore
    private List<Section> assignedSections = new ArrayList<>();

    // ADD THIS — Mapping with Students
    @OneToMany(mappedBy = "teacher", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Student> students = new ArrayList<>();

    @Lob
    @Column(name = "profile_picture", columnDefinition = "LONGBLOB")
    @Basic
    @Nullable
    private byte[] profilePicture;

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

    @ManyToMany
    @JoinTable(
            name = "teacher_assigned_classes",
            joinColumns = @JoinColumn(name = "teacher_id"),
            inverseJoinColumns = @JoinColumn(name = "class_id")
    )
    @JsonIgnore
    private List<SchoolClass> assignedClass = new ArrayList<>();

    // --- Sessions mapping (one teacher can have many sessions)
    @OneToMany(mappedBy = "teacher", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Session> sessions = new ArrayList<>();


}
