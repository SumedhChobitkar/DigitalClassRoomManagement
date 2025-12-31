package com.DigitalClassRoomManagement.Controller;

import com.DigitalClassRoomManagement.Entity.*;
import com.DigitalClassRoomManagement.Repository.*;
import com.DigitalClassRoomManagement.Service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletResponse;

import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.Map;
import java.util.Optional;

@RestController
public class CallbackController {

    private static final Logger log = LoggerFactory.getLogger(CallbackController.class);

    private final GoogleTokenService tokenService;
    private final GoogleRefreshTokenService refreshTokenService;
    private final OAuthStateService oauthStateService;   //CHANGED
    private final TeacherRepository teacherRepository;
    private final SessionRepository sessionRepository;
    private final GoogleCalendarService calendarService;

    @Value("${google.redirect.uri}")
    private String redirectUri;

    public CallbackController(GoogleTokenService tokenService,
                              GoogleRefreshTokenService refreshTokenService,
                              OAuthStateService oauthStateService,     //CHANGED
                              TeacherRepository teacherRepository,
                              SessionRepository sessionRepository,
                              GoogleCalendarService calendarService) {

        this.tokenService = tokenService;
        this.refreshTokenService = refreshTokenService;
        this.oauthStateService = oauthStateService;      //CHANGED
        this.teacherRepository = teacherRepository;
        this.sessionRepository = sessionRepository;
        this.calendarService = calendarService;
    }

    @GetMapping("/oauth/callback")
    public String callback(@RequestParam(value = "code", required = false) String code,
                           @RequestParam(value = "state", required = false) String state,
                           @RequestParam(value = "error", required = false) String error,
                           HttpServletResponse response) {

        log.info("OAuth callback: codePresent={}, state={}", code != null, state);

        if (error != null) {
            return "Google OAuth error: " + error;
        }

        if (code == null) {
            return "Missing code. Start flow via /google/login";
        }

        Optional<OAuthState> stOpt = oauthStateService.findByStateToken(state);   //CHANGED
        if (stOpt.isEmpty()) {
            log.warn("State not found or expired: {}", state);
            return "Invalid or expired state token";
        }

        OAuthState st = stOpt.get();

        try {
            Map<String, Object> tokens =
                    tokenService.exchangeCodeForTokens(code, redirectUri);

            if (tokens == null || !tokens.containsKey("refresh_token")) {
                return "No refresh token returned by Google. " +
                        "Use prompt=consent and access_type=offline";
            }

            String refreshToken = tokens.get("refresh_token").toString();


            //TEACHER FLOW

            if ("TEACHER".equalsIgnoreCase(st.getTargetType())) {

                Long teacherId = st.getTargetId();
                Teacher teacher = teacherRepository.findById(teacherId)
                        .orElseGet(() -> {
                            Teacher t = new Teacher();
                            t.setFirstName("Unknown");
                            return teacherRepository.save(t);
                        });

                refreshTokenService.saveRefreshToken(teacher, refreshToken);

                oauthStateService.deleteByStateToken(state);   //FIXED

                return "Connected Google account to Teacher ID: " + teacher.getId();
            }


              //SESSION FLOW

            if ("SESSION".equalsIgnoreCase(st.getTargetType())) {

                Long sessionId = st.getTargetId();
                Optional<Session> sOpt = sessionRepository.findById(sessionId);

                if (sOpt.isEmpty()) {
                    return "Session not found: " + sessionId;
                }

                Session session = sOpt.get();
                Teacher teacher = session.getTeacher();

                if (teacher == null) {
                    return "Session has no teacher assigned";
                }

                refreshTokenService.saveRefreshToken(teacher, refreshToken);

                OffsetDateTime start =
                        session.getStartTime()
                                .atOffset(ZoneId.systemDefault()
                                        .getRules()
                                        .getOffset(session.getStartTime()));

                OffsetDateTime end =
                        session.getEndTime()
                                .atOffset(ZoneId.systemDefault()
                                        .getRules()
                                        .getOffset(session.getEndTime()));

                String meetLink =
                        calendarService.createMeetEvent(
                                teacher.getId(),
                                start,
                                end,
                                session.getTopic()
                        );

                if (meetLink != null) {
                    session.setJoinLink(meetLink);
                    sessionRepository.save(session);

                    oauthStateService.deleteByStateToken(state);  //FIXED

                    response.sendRedirect(meetLink);
                    return null;
                }

                return "Connected but failed to create Google Meet for session";
            }

            return "Unsupported targetType: " + st.getTargetType();

        } catch (Exception ex) {
            log.error("Callback error", ex);
            return "Callback failed: " + ex.getMessage();
        }
    }
}
