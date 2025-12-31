package com.DigitalClassRoomManagement.Entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "google_refresh_tokens")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GoogleRefreshToken {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "teacher_id", nullable = false, unique = true)
    private Teacher teacher;

    @Column(name = "refresh_token", nullable = false, length = 2000)
    private String refreshToken;
}
