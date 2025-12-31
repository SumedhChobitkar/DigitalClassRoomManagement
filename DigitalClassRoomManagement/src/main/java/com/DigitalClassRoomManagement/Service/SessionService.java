package com.DigitalClassRoomManagement.Service;

import com.DigitalClassRoomManagement.Dto.SessionCreateRequestDto;
import com.DigitalClassRoomManagement.Dto.SessionResponseDto;

import java.util.List;

public interface SessionService {

    SessionResponseDto createSession(SessionCreateRequestDto dto);   //  ADD
    List<SessionResponseDto> getAllSessions();
    SessionResponseDto getSessionById(Long sessionId);
    List<SessionResponseDto> getSessionsByTeacher(Long teacherId);

}
