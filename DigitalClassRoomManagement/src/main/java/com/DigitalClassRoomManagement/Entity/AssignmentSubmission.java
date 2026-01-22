package com.DigitalClassRoomManagement.Entity;


import com.DigitalClassRoomManagement.Enum.SubmissionStatus;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Entity

@Table(name = "assignment_submissions",
        uniqueConstraints = @UniqueConstraint(columnNames = {"assignment_id", "student_id"}))
public class AssignmentSubmission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long submissionId;

    @ManyToOne
    @JoinColumn(name = "assignment_id", nullable = false)
    private Assignment assignment;


    @ManyToOne
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    //  FILE STORED AS BYTES
    @Lob
    @Column(name = "file_data", columnDefinition = "LONGBLOB", nullable = false)
    private byte[] fileData;

    // FILENAME
    @Column(name = "file_name", nullable = false)
    private String fileName;

    private LocalDateTime submittedAt;

    @Enumerated(EnumType.STRING)
    private SubmissionStatus status;

    @Column(nullable = false)
    private Double marks;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String feedback;

    }





