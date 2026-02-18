package com.DigitalClassRoomManagement.Service;

import com.DigitalClassRoomManagement.Dto.AttendanceDto;
import com.DigitalClassRoomManagement.Entity.Attendance;
import java.util.List;

public interface AttendanceService{
    public String  createAttendance(AttendanceDto dto);
    public List<Attendance> getAllAttendence();
    public Attendance getAttendanceByID(Long id);
    public String updateAttendanceById(Long id, AttendanceDto dto);
    public String deleteById(Long id);

    //Student will get their attendance info by their email
    AttendanceDto getAttendanceByEmail(String email);

    String joinSession(String email, Long sessionId);
    String leaveSession(String email, Long sessionId);
    String markAbsent(Long sessionId, String email);

    List<Attendance> getStudentsBySession(Long sessionId);
    long getTotalPeriodsAttended(String email);
    long getTotalClassesAttended(String email);
    String applyLeave(Long sessionId, String email, String reason);

}
