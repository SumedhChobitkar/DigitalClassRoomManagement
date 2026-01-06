package com.DigitalClassRoomManagement.Repository;

import com.DigitalClassRoomManagement.Entity.OAuthState;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface OAuthStateRepository extends JpaRepository<OAuthState, Long> {
    Optional<OAuthState> findByStateToken(String stateToken);
    void deleteByStateToken(String stateToken);
}
