package com.DigitalClassRoomManagement.Entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;
//import org.apache.catalina.User;


//import org.apache.catalina.User;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@Table(name = "parents")
public class Parent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "parent_id")
    private Long parentId;

    // One-to-One relationship with User entity
    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    @JsonManagedReference(value = "user-parent")
    private User users;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    private String phone;

    private String address;

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

    /* // One-to-Many relationship with ParentStudentMapping
    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ParentStudentMapping> studentMappings;     */

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
