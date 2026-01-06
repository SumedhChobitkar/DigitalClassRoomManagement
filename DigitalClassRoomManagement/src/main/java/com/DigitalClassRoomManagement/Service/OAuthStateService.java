package com.DigitalClassRoomManagement.Service;

import com.DigitalClassRoomManagement.Entity.OAuthState;
import com.DigitalClassRoomManagement.Repository.OAuthStateRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class OAuthStateService {

    private final OAuthStateRepository oauthStateRepository;

    public OAuthStateService(OAuthStateRepository oauthStateRepository) {
        this.oauthStateRepository = oauthStateRepository;
    }

    /**
     * Find OAuthState by state token
     */
    public Optional<OAuthState> findByStateToken(String stateToken) {
        return oauthStateRepository.findByStateToken(stateToken);
    }

    /**
     * Save OAuthState (used while initiating Google OAuth login)
     */
    public OAuthState save(OAuthState oauthState) {
        return oauthStateRepository.save(oauthState);
    }

    /**
     * Delete OAuthState after callback is processed
     * IMPORTANT: @Transactional is REQUIRED for delete
     */
    @Transactional
    public void deleteByStateToken(String stateToken) {
        oauthStateRepository.deleteByStateToken(stateToken);
    }
}
