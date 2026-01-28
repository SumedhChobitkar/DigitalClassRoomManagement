package com.DigitalClassRoomManagement.Entity;

import com.DigitalClassRoomManagement.Enum.Relationship;
import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;


@Data
@Entity
@Table(name = "parents")
public class Parent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "parent_id")
    private Long parentId;


    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    //@Column(nullable = false)
    //private String name;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;


    @Column(nullable = false, unique = true)
    private String email;

    private String phone;

    private String address;

    private String teacherMailId;

    @OneToOne
    @JoinColumn(name = "student_id", referencedColumnName = "studentRegId")
    private Student student;




    // Enum type — FATHER, MOTHER, GUARDIAN
    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private Relationship relationship;


    // Audit fields
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;


    // Auto timestamps
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
