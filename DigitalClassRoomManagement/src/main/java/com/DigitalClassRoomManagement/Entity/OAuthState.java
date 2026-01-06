package com.DigitalClassRoomManagement.Entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.Instant;
@Entity
@Table(name = "oauth_states")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OAuthState {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="state_token", nullable=false, unique=true, length=128)
    private String stateToken;

    @Column(name="target_type", nullable=false)
    private String targetType; // "TEACHER" or "SESSION"

    @Column(name="target_id", nullable=false)
    private Long targetId;

    @Column(name="created_at", nullable=false)
    private Instant createdAt;
}
