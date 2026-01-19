package com.DigitalClassRoomManagement.ServiceImpl;

import com.DigitalClassRoomManagement.Dto.EventResponse;
import com.DigitalClassRoomManagement.Entity.GoogleRefreshToken;
import com.DigitalClassRoomManagement.Entity.Session;
import com.DigitalClassRoomManagement.Entity.Student;
import com.DigitalClassRoomManagement.Entity.Teacher;
import com.DigitalClassRoomManagement.Repository.GoogleRefreshTokenRepository;
import com.DigitalClassRoomManagement.Repository.SessionRepository;
import com.DigitalClassRoomManagement.Repository.StudentRepository;
import com.DigitalClassRoomManagement.Repository.TeacherRepository;
import com.DigitalClassRoomManagement.Service.EmailSenderService;
import com.DigitalClassRoomManagement.Service.GoogleCalendarService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestTemplate;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.*;

@Service
public class GoogleCalendarServiceImpl implements GoogleCalendarService {

    private static final Logger log =
            LoggerFactory.getLogger(GoogleCalendarServiceImpl.class);

    private final GoogleRefreshTokenRepository refreshTokenRepo;
    private final TeacherRepository teacherRepo;
    private final SessionRepository sessionRepo;
    private final StudentRepository studentRepo;          // NEW
    private final EmailSenderService emailService;        // NEW
    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${google.client.id}")
    private String clientId;

    @Value("${google.client.secret}")
    private String clientSecret;

    public GoogleCalendarServiceImpl(GoogleRefreshTokenRepository refreshTokenRepo,
                                     TeacherRepository teacherRepo,
                                     SessionRepository sessionRepo,
                                     StudentRepository studentRepo,
                                     EmailSenderService emailService) {

        this.refreshTokenRepo = refreshTokenRepo;
        this.teacherRepo = teacherRepo;
        this.sessionRepo = sessionRepo;
        this.studentRepo = studentRepo;
        this.emailService = emailService;
    }

    // ACCESS TOKEN
    private String getAccessTokenForTeacher(Long teacherId) {

        Teacher teacher = teacherRepo.findById(teacherId)
                .orElseThrow(() ->
                        new RuntimeException("Teacher not found: " + teacherId));

        GoogleRefreshToken refreshToken = refreshTokenRepo.findByTeacher(teacher)
                .orElseThrow(() ->
                        new RuntimeException("Refresh token missing for teacher"));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        LinkedMultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("client_id", clientId);
        body.add("client_secret", clientSecret);
        body.add("refresh_token", refreshToken.getRefreshToken());
        body.add("grant_type", "refresh_token");

        HttpEntity<?> req = new HttpEntity<>(body, headers);

        ResponseEntity<Map> resp =
                restTemplate.postForEntity(
                        "https://oauth2.googleapis.com/token",
                        req,
                        Map.class
                );

        Map tokenResp = resp.getBody();

        if (tokenResp == null || !tokenResp.containsKey("access_token")) {
            throw new RuntimeException("Invalid Google token response");
        }

        log.info("Granted scopes = {}", tokenResp.get("scope"));
        return (String) tokenResp.get("access_token");
    }

    // ================= LOW LEVEL MEET =================
    @Override
    public String createMeetEvent(Long teacherId,
                                  OffsetDateTime start,
                                  OffsetDateTime end,
                                  String title) {

        String accessToken = getAccessTokenForTeacher(teacherId);

        String url =
                "https://www.googleapis.com/calendar/v3/calendars/primary/events?conferenceDataVersion=1";

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> event = new HashMap<>();
        event.put("summary", title);

        event.put("start", Map.of(
                "dateTime", start.toInstant().toString(),
                "timeZone", "Asia/Kolkata"
        ));

        event.put("end", Map.of(
                "dateTime", end.toInstant().toString(),
                "timeZone", "Asia/Kolkata"
        ));

        event.put("conferenceData", Map.of(
                "createRequest", Map.of(
                        "requestId", UUID.randomUUID().toString(),
                        "conferenceSolutionKey", Map.of("type", "hangoutsMeet")
                )
        ));

        HttpEntity<Map<String, Object>> req =
                new HttpEntity<>(event, headers);

        ResponseEntity<Map> resp =
                restTemplate.postForEntity(url, req, Map.class);

        log.info("Calendar API STATUS = {}", resp.getStatusCode());
        log.info("Calendar API BODY = {}", resp.getBody());

        Map body = resp.getBody();
        Map conferenceData = (Map) body.get("conferenceData");
        List<Map> entryPoints = (List<Map>) conferenceData.get("entryPoints");

        for (Map ep : entryPoints) {
            if ("video".equals(ep.get("entryPointType"))) {
                return (String) ep.get("uri");
            }
        }
        return null;
    }

    // SESSION BASED
    @Override
    @Transactional
    public Map<String, Object> createMeetEvent(Long sessionId) {

        Session session = sessionRepo.findById(sessionId)
                .orElseThrow(() ->
                        new RuntimeException("Session not found: " + sessionId));

        if (session.getDate().isBefore(LocalDate.now())) {
            throw new RuntimeException("Cannot create Google Meet for past session");
        }

        ZoneId zone = ZoneId.of("Asia/Kolkata");

        OffsetDateTime start =
                session.getStartTime().atZone(zone).toOffsetDateTime();

        OffsetDateTime end =
                session.getEndTime().atZone(zone).toOffsetDateTime();

        String meetLink =
                createMeetEvent(
                        session.getTeacher().getId(),
                        start,
                        end,
                        session.getTopic()
                );

        session.setJoinLink(meetLink);
        session.setGoogleEventId(UUID.randomUUID().toString());

        sessionRepo.save(session);


        // SEND MEET LINK TO STUDENTS
        List<Student> students =
                studentRepo.findBySection(session.getSection());

        for (Student s : students) {

            if (s.getEmail() == null || s.getEmail().isBlank())
                continue;

            String body =
                    "Dear Student,\n\n" +
                            "Your class '" + session.getTopic() + "' is scheduled.\n\n" +
                            "Date  : " + session.getDate() + "\n" +
                            "Start : " + session.getStartTime() + "\n\n" +
                            "Join Google Meet using below link:\n" +
                            session.getJoinLink() + "\n\n" +
                            "Regards,\nSchool Administration";

            emailService.sendEmail(
                    s.getEmail(),
                    "Your Class Google Meet Link",
                    body
            );
        }

        log.info("Google Meet link sent to {} students", students.size());


        return Map.of(
                "message", "Google Meet created successfully",
                "meetLink", meetLink
        );
    }

    // REQUIRED BY INTERFACE
    @Override
    public EventResponse createEventForSession(Session session) {

        if (session == null || session.getTeacher() == null) {
            return null;
        }

        if (session.getDate().isBefore(LocalDate.now())) {
            throw new RuntimeException("Cannot create Google Meet for past session");
        }

        ZoneId zone = ZoneId.of("Asia/Kolkata");

        OffsetDateTime start =
                session.getStartTime().atZone(zone).toOffsetDateTime();

        OffsetDateTime end =
                session.getEndTime().atZone(zone).toOffsetDateTime();

        String joinUrl =
                createMeetEvent(
                        session.getTeacher().getId(),
                        start,
                        end,
                        session.getTopic()
                );

        return joinUrl == null ? null : new EventResponse(joinUrl, null);
    }
}
