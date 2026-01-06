package com.DigitalClassRoomManagement.Controller;

import com.DigitalClassRoomManagement.Dto.SessionCreateRequestDto;
import com.DigitalClassRoomManagement.Dto.SessionResponseDto;
import com.DigitalClassRoomManagement.Service.SessionService;
import com.DigitalClassRoomManagement.Service.GoogleCalendarService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/sessions")
@CrossOrigin("*")
@RequiredArgsConstructor
@Slf4j
public class SessionController {

    private final SessionService sessionService;
    private final GoogleCalendarService googleCalendarService;

    //CREATE SESSION
    @PostMapping("/create")
    public ResponseEntity<?> createSession(@RequestBody SessionCreateRequestDto dto) {
        try {
            log.info("Creating session for teacherId={}, topic={}",
                    dto.getTeacherId(), dto.getTopic());

            SessionResponseDto response = sessionService.createSession(dto);

            log.info("Session created successfully sessionId={}",
                    response.getSessionId());

            return ResponseEntity.status(HttpStatus.CREATED).body(response);

        } catch (Exception ex) {
            log.error("Error while creating session", ex);
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to create session: " + ex.getMessage());
        }
    }

    //GET ALL SESSIONS
    @GetMapping("/getAll")
    public ResponseEntity<?> getAll() {
        try {
            log.info("Fetching all sessions");

            List<SessionResponseDto> sessions = sessionService.getAllSessions();

            log.info("Total sessions found={}", sessions.size());

            return ResponseEntity.ok(sessions);

        } catch (Exception ex) {
            log.error("Error while fetching all sessions", ex);
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to fetch sessions");
        }
    }

    //GET SESSION BY ID
    @GetMapping("/getById/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        try {
            log.info("Fetching session with id={}", id);

            SessionResponseDto response = sessionService.getSessionById(id);

            log.info("Session fetched successfully id={}, meetLinkPresent={}",
                    id, response.getJoinLink() != null);

            return ResponseEntity.ok(response);

        } catch (Exception ex) {
            log.error("Error while fetching session with id={}", id, ex);
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body("Session not found with id=" + id);
        }
    }

    //CREATE GOOGLE MEET (FOR TESTING)
    @GetMapping("/{id}/create-meet")
    public ResponseEntity<?> createMeet(@PathVariable Long id) {
        try {
            log.info("Creating Google Meet for sessionId={}", id);

            Object meetResponse = googleCalendarService.createMeetEvent(id);

            log.info("Google Meet created successfully for sessionId={}", id);

            return ResponseEntity.ok(meetResponse);

        } catch (Exception ex) {
            log.error("Error while creating Google Meet for sessionId={}", id, ex);
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to create Google Meet: " + ex.getMessage());
        }
    }

    // NEW API ADDED — Get Sessions by Teacher ID:-
    @GetMapping("/teacher/{teacherId}")
    public ResponseEntity<?> getSessionsByTeacher(@PathVariable Long teacherId) {
        try {
            log.info("Fetching sessions for teacherId={}", teacherId);

            List<SessionResponseDto> sessions =
                    sessionService.getSessionsByTeacher(teacherId);

            log.info("Total sessions found for teacherId {} = {}", teacherId, sessions.size());

            return ResponseEntity.ok(sessions);

        } catch (Exception ex) {
            log.error("Error while fetching sessions for teacherId={}", teacherId, ex);
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to fetch teacher sessions");
        }
    }

}
