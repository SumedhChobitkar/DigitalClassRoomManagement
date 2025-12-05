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

    String joinSession(String email, Long sessionId);

    String leaveSession(String email, Long sessionId);

    String markAbsent(Long sessionId, String email);

    List<Attendance> getStudentsBySession(Long sessionId);
}
