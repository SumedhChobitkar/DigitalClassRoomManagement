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

import javax.management.RuntimeMBeanException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class AttendanceServiceImpl implements AttendanceService {

    private static final Logger log = LoggerFactory.getLogger(AttendanceServiceImpl.class);

    @Autowired
    private AttendanceRepository repo;

    @Override
    public String createAttendance(AttendanceDto dto) {
        log.info("Creating new attendance for date: {}", dto.getDate());
        try {
            Attendance attendance = new Attendance();
            attendance.setDate(dto.getDate());
            attendance.setJoinTime(dto.getJoinTime());
            attendance.setStatus(dto.getStatus());
            attendance.setExitTime(dto.getExitTime());
            attendance.setDurationMinutes(dto.getDurationMinutes());
            attendance.setMarkedBy(dto.getMarkedBy());

            Attendance savedAttendance = repo.save(attendance);
            log.info("Attendance saved successfully with ID: {}", savedAttendance.getAttendanceId());
            return "Attendance added successfully with ID: " + savedAttendance.getAttendanceId();
        } catch (Exception e) {
            log.error("Failed to create attendance: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to add attendance: " + e.getMessage());
        }
    }

    @Override
    public List<Attendance> getAllAttendence() {
        log.info("Retrieving all attendance records from database...");
        List<Attendance> attendanceList = repo.findAll();
        log.info("Retrieved {} attendance records.", attendanceList.size());
        return attendanceList;
    }

    @Override
    public Attendance getAttendanceByID(Long id) {
        log.info("Fetching attendance details for ID: {}", id);
        return repo.findById(id)
                .orElseThrow(() -> {
                    log.warn("Attendance not found with ID: {}", id);
                    return new AttendanceNotFoundException("Attendance not found with ID: " + id);
                });
    }

    @Override
    public String updateAttendanceById(Long id, AttendanceDto dto) {
        log.info("Updating attendance record with ID: {}", id);
        try {
            Attendance existing = repo.findById(id)
                    .orElseThrow(() -> {
                        log.warn("Attendance not found for update with ID: {}", id);
                        return new AttendanceNotFoundException("Attendance not found with ID: " + id);
                    });

            existing.setDate(dto.getDate());
            existing.setJoinTime(dto.getJoinTime());
            existing.setExitTime(dto.getExitTime());
            existing.setStatus(dto.getStatus());
            existing.setDurationMinutes(dto.getDurationMinutes());
            existing.setMarkedBy(dto.getMarkedBy());

            repo.save(existing);
            log.info("Attendance updated successfully for ID: {}", id);
            return "Attendance updated successfully with ID: " + id;
        } catch (AttendanceNotFoundException ex) {
            log.error("Attendance not found while updating ID: {}", id);
            throw ex;
        } catch (Exception e) {
            log.error("Error while updating attendance with ID: {} - {}", id, e.getMessage(), e);
            throw new RuntimeException("Updation failed: " + e.getMessage());
        }
    }

    @Override
    public String deleteById(Long id) {
        log.info("Deleting attendance record with ID: {}", id);
        try {
            Attendance existing = repo.findById(id)
                    .orElseThrow(() -> {
                        log.warn("Attendance not found for deletion with ID: {}", id);
                        return new AttendanceNotFoundException("Attendance not found with ID: " + id);
                    });

            repo.delete(existing);
            log.info("Attendance deleted successfully with ID: {}", id);
            return "Attendance deleted successfully with ID: " + id;
        } catch (AttendanceNotFoundException ex) {
            log.warn("Attempt to delete non-existing attendance ID: {}", id);
            throw ex;
        } catch (Exception e) {
            log.error("Error occurred while deleting attendance with ID: {} - {}", id, e.getMessage(), e);
            throw new RuntimeException("Deletion failed: " + e.getMessage());
        }
    }

    @Override
    public String joinSession(String email, Long sessionId) {
        if (repo.findBySessionIdAndEmail(sessionId, email).isPresent()) {
            return "User have already joined the session";
        }
            Attendance attendance = new Attendance();
            attendance.setEmail(email);
            attendance.setSessionId(sessionId);
            attendance.setJoinTime(LocalDateTime.now());
            attendance.setStatus(AttendanceStatus.PRESENT);
            attendance.setDurationMinutes(0L);
            attendance.setMarkedBy(MarkBy.TEACHER);

            repo.save(attendance);
            return "Student joined session succesfully";
        }



    @Override
    public String leaveSession(String email, Long sessionId) {
        Attendance attendance=repo.findBySessionIdAndEmail(sessionId,email)
                .orElseThrow(() -> new RuntimeException("User did not join session!"));
//        Session session = sessionRepository.findById(sessionId)
//                .orElseThrow(() -> new RuntimeException("Session not found!"));
        attendance.setExitTime(LocalDateTime.now());

        long expectedTimeDuration= Duration.between(
                attendance.getStartTime(),
                attendance.getEndTime()).toMinutes();

        long actualTimeDuration= Duration.between(
                attendance.getJoinTime(),
                attendance.getExitTime()
        ).toMinutes();

        if(actualTimeDuration<expectedTimeDuration/2){
            attendance.setStatus(AttendanceStatus.HALF_DAY);
        }else{
            attendance.setStatus(AttendanceStatus.PRESENT);
        }
        repo.save(attendance);
        return "Leave successful → Actual: " + actualTimeDuration +
                " mins, Expected: " + expectedTimeDuration +
                " mins → Status: " + attendance.getStatus();
    }

    @Override
    public String markAbsent(Long sessionId, String email) {
        Attendance attendance = repo
                .findBySessionIdAndEmail(sessionId, email)
                .orElse(new Attendance());

        attendance.setSessionId(sessionId);
        attendance.setEmail(email);
        attendance.setStatus(AttendanceStatus.ABSENT);
        attendance.setDurationMinutes(0L);
        attendance.setJoinTime(null);
        attendance.setExitTime(null);
        attendance.setMarkedBy(MarkBy.TEACHER);

        repo.save(attendance);

        return "Marked as ABSENT for session " + sessionId;
    }

    @Override
    public List<Attendance> getStudentsBySession(Long sessionId) {
        return repo.findAllBySessionId(sessionId);
    }
}
