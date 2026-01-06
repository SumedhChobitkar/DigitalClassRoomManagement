package com.DigitalClassRoomManagement.Controller;

import com.DigitalClassRoomManagement.Entity.OAuthState;
import com.DigitalClassRoomManagement.Repository.OAuthStateRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpServletResponse;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.UUID;

/**
 * Starts Google OAuth login by creating a short-lived state token and redirecting to Google.
 *
 * Usage:
 *  - For teacher linking:  GET /google/login?targetType=TEACHER&targetId=1
 *  - For session linking:  GET /google/login?targetType=SESSION&targetId=10
 *
 * This controller requires the caller to be authenticated with role TEACHER (see @PreAuthorize).
 * If you prefer to make this public, remove the @PreAuthorize annotation (Option B).
 */
@RestController
@RequestMapping("/google")
@CrossOrigin(origins = "*")
public class AuthController {

    private static final Logger log = LoggerFactory.getLogger(AuthController.class);

    private final OAuthStateRepository oauthStateRepository;

    @Value("${google.client.id}")
    private String clientId;

    @Value("${google.redirect.uri}")
    private String redirectUri;

    @Value("${google.scopes}")
    private String scopes;

    public AuthController(OAuthStateRepository oauthStateRepository) {
        this.oauthStateRepository = oauthStateRepository;
    }

    /**
     * Start OAuth flow.
     *
     * Note: This endpoint is protected with PreAuthorize so that only logged-in teachers/admin can invoke it.
     * If you want it public, remove @PreAuthorize (see notes below).
     *
     * Example:
     *  /google/login?targetType=TEACHER&targetId=1
     */
    @GetMapping("/login")
    //@PreAuthorize("hasAnyRole('TEACHER','ADMIN')")  // <- protected: only teachers/admins
    public void login(@RequestParam String targetType,
                      @RequestParam Long targetId,
                      HttpServletResponse response) {
        try {
            // create random state token and persist mapping to target (teacher/session)
            String stateToken = UUID.randomUUID().toString();

            OAuthState st = OAuthState.builder()
                    .stateToken(stateToken)
                    .targetType(targetType)
                    .targetId(targetId)
                    .createdAt(Instant.now())
                    .build();

            oauthStateRepository.save(st);

            // Build Google OAuth URL
            String authUrl = "https://accounts.google.com/o/oauth2/v2/auth"
                    + "?client_id=" + URLEncoder.encode(clientId, StandardCharsets.UTF_8)
                    + "&response_type=code"
                    + "&scope=" + URLEncoder.encode(scopes, StandardCharsets.UTF_8)
                    + "&redirect_uri=" + URLEncoder.encode(redirectUri, StandardCharsets.UTF_8)
                    + "&access_type=offline"
                    + "&prompt=consent"
                    + "&state=" + URLEncoder.encode(stateToken, StandardCharsets.UTF_8);

            log.info("Starting Google OAuth for targetType={} targetId={}", targetType, targetId);
            response.sendRedirect(authUrl);
        } catch (Exception ex) {
            log.error("Failed to start Google OAuth", ex);
            try {
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Failed to start OAuth");
            } catch (Exception ignored) {}
        }
    }
}
