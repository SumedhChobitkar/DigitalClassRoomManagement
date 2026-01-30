package com.DigitalClassRoomManagement.ServiceImpl;

import com.DigitalClassRoomManagement.Dto.AttendanceDto;
import com.DigitalClassRoomManagement.Entity.Attendance;
import com.DigitalClassRoomManagement.Enum.AttendanceStatus;
import com.DigitalClassRoomManagement.Enum.MarkBy;
import com.DigitalClassRoomManagement.Exception.AttendanceNotFoundException;
import com.DigitalClassRoomManagement.Repository.AttendanceRepository;
import com.DigitalClassRoomManagement.Service.AttendanceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class AttendanceServiceImpl implements AttendanceService {

    private static final Logger log = LoggerFactory.getLogger(AttendanceServiceImpl.class);

    @Autowired
    private AttendanceRepository repo;

    //MANUAL CREATE (KEEPED – NOT RECOMMENDED)
    @Override
    public String createAttendance(AttendanceDto dto) {
        log.info("Manual attendance create request sessionId={}, email={}",
                dto.getSessionId(), dto.getEmail());
        try {
            Attendance attendance = new Attendance();
            attendance.setClassDate(dto.getDate());
            attendance.setJoinTime(dto.getJoinTime());
            attendance.setExitTime(dto.getExitTime());
            attendance.setDurationMinutes(dto.getDurationMinutes());
            attendance.setStatus(dto.getStatus());
            attendance.setMarkedBy(dto.getMarkedBy());
            attendance.setEmail(dto.getEmail());
            attendance.setSessionId(dto.getSessionId());

            repo.save(attendance);
            log.info("Manual attendance saved successfully");
            return "Attendance added successfully";

        } catch (Exception e) {
            log.error("Error while creating attendance", e);
            throw new RuntimeException("Failed to add attendance: " + e.getMessage());
        }
    }

    @Override
    public List<Attendance> getAllAttendence() {
        log.info("Fetching all attendance records");
        return repo.findAll();
    }

    @Override
    public Attendance getAttendanceByID(Long id) {
        log.info("Fetching attendance by id={}", id);
        return repo.findById(id)
                .orElseThrow(() ->
                        new AttendanceNotFoundException("Attendance not found with ID: " + id));
    }

    @Override
    public String updateAttendanceById(Long id, AttendanceDto dto) {
        log.info("Updating attendance id={}", id);
        Attendance existing = repo.findById(id)
                .orElseThrow(() ->
                        new AttendanceNotFoundException("Attendance not found with ID: " + id));

        existing.setClassDate(dto.getDate());
        existing.setJoinTime(dto.getJoinTime());
        existing.setExitTime(dto.getExitTime());
        existing.setStatus(dto.getStatus());
        existing.setDurationMinutes(dto.getDurationMinutes());
        existing.setMarkedBy(dto.getMarkedBy());

        repo.save(existing);
        log.info("Attendance updated successfully id={}", id);
        return "Attendance updated successfully";
    }

    @Override
    public String deleteById(Long id) {
        log.info("Deleting attendance id={}", id);
        Attendance existing = repo.findById(id)
                .orElseThrow(() ->
                        new AttendanceNotFoundException("Attendance not found with ID: " + id));
        repo.delete(existing);
        log.info("Attendance deleted id={}", id);
        return "Attendance deleted successfully";
    }


    //CORE LOGIC – AUTO ATTENDANCE ON JOIN + LATE LOGIC
    @Override
    public String joinSession(String email, Long sessionId) {
        log.info("Join session request sessionId={}, email={}", sessionId, email);
        try {
            if (repo.findBySessionIdAndEmail(sessionId, email).isPresent()) {
                log.warn("Student already joined sessionId={}, email={}", sessionId, email);
                return "User already joined this session";
            }

            LocalDateTime joinTime = LocalDateTime.now();

            Attendance attendance = new Attendance();
            attendance.setEmail(email);
            attendance.setSessionId(sessionId);
            attendance.setJoinTime(joinTime);
            attendance.setClassDate(LocalDate.now());
            attendance.setDurationMinutes(0L);
            attendance.setMarkedBy(MarkBy.TEACHER);

            /*
             * LATE RULE:
             * Join after 10 minutes → LATE
             */
            if (joinTime.getMinute() > 10) {
                attendance.setStatus(AttendanceStatus.LATE);
                log.info("Student joined LATE sessionId={}, email={}", sessionId, email);
            } else {
                attendance.setStatus(AttendanceStatus.PRESENT);
                log.info("Student joined ON TIME sessionId={}, email={}", sessionId, email);
            }

            repo.save(attendance);
            return "Student joined session successfully";

        } catch (Exception e) {
            log.error("Error during join session", e);
            throw new RuntimeException("Join session failed");
        }
    }

    //LEAVE SESSION – FINAL STATUS LOGIC
    //(ABSENT + HALF_DAY + EARLY_LEAVE + PRESENT/LATE)
    @Override
    public String leaveSession(String email, Long sessionId) {
        log.info("Leave session request sessionId={}, email={}", sessionId, email);
        try {
            Attendance attendance = repo.findBySessionIdAndEmail(sessionId, email)
                    .orElseThrow(() ->
                            new RuntimeException("User did not join session"));

            attendance.setExitTime(LocalDateTime.now());

            long actualMinutes = Duration.between(
                    attendance.getJoinTime(),
                    attendance.getExitTime()
            ).toMinutes();

            attendance.setDurationMinutes(actualMinutes);

            /*
             * FINAL STATUS RULE:
             *
             * < 20 min   → ABSENT
             * 20–40 min  → HALF_DAY
             * 40–60 min  → EARLY_LEAVE
             * >= 60 min  → PRESENT / LATE
             */
            if (actualMinutes < 20) {
                attendance.setStatus(AttendanceStatus.ABSENT);

            } else if (actualMinutes < 40) {
                attendance.setStatus(AttendanceStatus.HALF_DAY);

            } else if (actualMinutes < 60) {
                attendance.setStatus(AttendanceStatus.EARLY_LEAVE);

            } else {

                //LATE
                if (attendance.getStatus() != AttendanceStatus.LATE) {
                    attendance.setStatus(AttendanceStatus.PRESENT);
                }
            }

            repo.save(attendance);
            log.info("Leave processed sessionId={}, email={}, duration={} min, status={}",
                    sessionId, email, actualMinutes, attendance.getStatus());

            return "Leave successful → Duration: " + actualMinutes +
                    " mins → Status: " + attendance.getStatus();

        } catch (Exception e) {
            log.error("Error during leave session", e);
            throw new RuntimeException("Leave session failed");
        }
    }


    //LEAVE APPLY – NEW FUNCTIONALITY
    @Override
    public String applyLeave(Long sessionId, String email, String reason) {
        log.info("Apply leave request sessionId={}, email={}, reason={}",
                sessionId, email, reason);
        try {
            Attendance attendance = repo.findBySessionIdAndEmail(sessionId, email)
                    .orElseGet(() -> {
                        log.info("No attendance found, creating LEAVE record");
                        Attendance a = new Attendance();
                        a.setSessionId(sessionId);
                        a.setEmail(email);
                        a.setClassDate(LocalDate.now());
                        a.setDurationMinutes(0L);
                        a.setMarkedBy(MarkBy.TEACHER);
                        return a;
                    });

            attendance.setStatus(AttendanceStatus.LEAVE);

            repo.save(attendance);
            log.info("Leave applied successfully sessionId={}, email={}",
                    sessionId, email);

            return "Leave applied successfully";

        } catch (Exception e) {
            log.error("Error while applying leave", e);
            throw new RuntimeException("Apply leave failed");
        }
    }


    //ABSENT – ONLY IF STUDENT NEVER JOINED
    @Override
    public String markAbsent(Long sessionId, String email) {
        log.info("Mark absent request sessionId={}, email={}", sessionId, email);
        try {
            if (repo.findBySessionIdAndEmail(sessionId, email).isPresent()) {
                log.warn("Cannot mark absent, student already joined session");
                return "Student already joined session, cannot mark absent";
            }

            Attendance attendance = new Attendance();
            attendance.setSessionId(sessionId);
            attendance.setEmail(email);
            attendance.setClassDate(LocalDate.now());
            attendance.setStatus(AttendanceStatus.ABSENT);
            attendance.setDurationMinutes(0L);
            attendance.setMarkedBy(MarkBy.TEACHER);

            repo.save(attendance);
            log.info("Student marked ABSENT sessionId={}, email={}", sessionId, email);
            return "Marked ABSENT for session " + sessionId;

        } catch (Exception e) {
            log.error("Error while marking absent", e);
            throw new RuntimeException("Mark absent failed");
        }
    }

    @Override
    public List<Attendance> getStudentsBySession(Long sessionId) {
        log.info("Fetching attendance list for sessionId={}", sessionId);
        return repo.findAllBySessionId(sessionId);
    }

    //COUNTS
    //PeriodsAttended
    @Override
    public long getTotalPeriodsAttended(String email) {
        log.info("Counting periods attended for email={}", email);
        return repo.countByEmailAndStatus(email, AttendanceStatus.PRESENT);
    }

    //ClassesAttended
    @Override
    public long getTotalClassesAttended(String email) {
        log.info("Counting classes attended for email={}", email);
        return repo.countDistinctClassDays(email);
    }
}
