package com.DigitalClassRoomManagement.Controller;

import com.DigitalClassRoomManagement.Entity.Session;
import com.DigitalClassRoomManagement.Entity.Teacher;
import com.DigitalClassRoomManagement.Repository.SessionRepository;
import com.DigitalClassRoomManagement.Repository.TeacherRepository;
import com.DigitalClassRoomManagement.Service.GoogleCalendarService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/meet")
@CrossOrigin(origins = "*")
public class MeetController {

    private static final Logger log =
            LoggerFactory.getLogger(MeetController.class);

    private final GoogleCalendarService calendarService;
    private final SessionRepository sessionRepo;
    private final TeacherRepository teacherRepo;

    public MeetController(GoogleCalendarService calendarService,
                          SessionRepository sessionRepo,
                          TeacherRepository teacherRepo) {
        this.calendarService = calendarService;
        this.sessionRepo = sessionRepo;
        this.teacherRepo = teacherRepo;
    }

    /**
     * Create Google Meet
     * 1) By sessionId
     * 2) OR by teacherId + start + end
     */
    @GetMapping("/create")
    public Map<String, Object> createMeet(
            @RequestParam(required = false) Long sessionId,
            @RequestParam(required = false) Long teacherId,
            @RequestParam(required = false) String start,
            @RequestParam(required = false) String end,
            @RequestParam(defaultValue = "Class Session") String title
    ) {

        Map<String, Object> resp = new HashMap<>();

        try {

            //SESSION FLOW (UNCHANGED)
            if (sessionId != null) {

                Session session = sessionRepo.findById(sessionId)
                        .orElseThrow(() ->
                                new RuntimeException("Session not found: " + sessionId));

                //SAFETY (MAIN FIX)
                if (session.getDate().isBefore(java.time.LocalDate.now())) {
                    throw new RuntimeException(
                            "Cannot create Google Meet for past session"
                    );
                }

                Teacher teacher = session.getTeacher();
                if (teacher == null) {
                    throw new RuntimeException("Session has no teacher assigned");
                }

                ZoneId zone = ZoneId.of("Asia/Kolkata");

                OffsetDateTime startOd =
                        session.getStartTime()
                                .atZone(zone)
                                .toOffsetDateTime();

                OffsetDateTime endOd =
                        session.getEndTime()
                                .atZone(zone)
                                .toOffsetDateTime();

                log.info(
                        "Creating Meet for sessionId={}, teacherId={}, start={}, end={}",
                        sessionId, teacher.getId(), startOd, endOd
                );

                //Service handles Google Meet + DB save
                Map<String, Object> result =
                        calendarService.createMeetEvent(sessionId);

                resp.putAll(result);
                return resp;
            }

            //DIRECT TEACHER FLOW (UNCHANGED)
            if (teacherId != null && start != null && end != null) {

                Teacher teacher = teacherRepo.findById(teacherId)
                        .orElseThrow(() ->
                                new RuntimeException("Teacher not found: " + teacherId));

                // expects ISO format
                OffsetDateTime startOd = OffsetDateTime.parse(start);
                OffsetDateTime endOd = OffsetDateTime.parse(end);

                log.info(
                        "Creating Meet for teacherId={}, start={}, end={}",
                        teacherId, startOd, endOd
                );

                String meetLink =
                        calendarService.createMeetEvent(
                                teacherId,
                                startOd,
                                endOd,
                                title
                        );

                if (meetLink == null) {
                    throw new RuntimeException("Google Meet creation failed");
                }

                resp.put("message", "Google Meet created");
                resp.put("meetLink", meetLink);
                return resp;
            }

            resp.put("error", "Provide sessionId OR teacherId + start + end");
            return resp;

        } catch (Exception ex) {
            log.error("Create meet failed", ex);
            resp.put("error", ex.getMessage());
            return resp;
        }
    }
}
