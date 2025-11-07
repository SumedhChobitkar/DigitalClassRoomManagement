package com.DigitalClassRoomManagement.Entity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import java.time.Instant;

@Entity
@Table(name = "multimedia_contents")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MultimediaContent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long contentId;

    @NotBlank
    @Size(max = 255)
    @Column(nullable = false)
    private String title;

    /**
     * Allowed values: VIDEO, AUDIO, ANIMATION, IMAGE, PDF, YOUTUBE
     */
    @NotBlank
    @Column(nullable = false, length = 20)
    private String type;

    /**
     * For YOUTUBE type, we store the link here.
     * For file types this is the original filename (optional helper).
     */
    @Size(max = 1000)
    private String url;

    private String contentType;   // e.g., video/mp4, audio/mpeg, application/pdf, image/png
    private Long fileSize;        // in bytes
    private String fileName;      // original file name if uploaded

    @Lob
    @Basic(fetch = FetchType.LAZY)
    @Column(columnDefinition = "LONGBLOB")
    private byte[] data;          // NULL for YOUTUBE

    @CreationTimestamp
    private Instant createdAt;

    @UpdateTimestamp
    private Instant updatedAt;
}
