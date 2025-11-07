package com.DigitalClassRoomManagement.Entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Data
public class DigitalBook {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bookId;

    private String title;
    @Lob
    private byte[] fileUrl;  // PDF/eBook URL
    private String grade;
    private String subject;
    private LocalDate uploadDate;
}
