package com.DigitalClassRoomManagement.ServiceImpl;

import com.DigitalClassRoomManagement.Dto.EventResponse;
import com.DigitalClassRoomManagement.Dto.SessionCreateRequestDto;
import com.DigitalClassRoomManagement.Dto.SessionResponseDto;
import com.DigitalClassRoomManagement.Entity.*;
import com.DigitalClassRoomManagement.Exception.SessionNotFoundException;
import com.DigitalClassRoomManagement.Repository.*;
import com.DigitalClassRoomManagement.Service.GoogleCalendarService;
import com.DigitalClassRoomManagement.Service.SessionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class SessionServiceImpl implements SessionService {

    private final SessionRepository sessionRepository;
    private final TeacherRepository teacherRepository;
    private final TimetableRepository timetableRepository;
    private final SchoolClassRepository schoolClassRepository;
    private final SectionRepository sectionRepository;
    private final GoogleCalendarService googleCalendarService;

    //CREATE SESSION
    @Override
    @Transactional
    public SessionResponseDto createSession(SessionCreateRequestDto dto) {

        Teacher teacher =
                teacherRepository.findById(dto.getTeacherId())
                        .orElseThrow(() ->
                                new RuntimeException("Teacher not found"));

        Timetable timetable =
                timetableRepository.findById(dto.getTimetableId())
                        .orElseThrow(() ->
                                new RuntimeException("Timetable not found"));

        SchoolClass schoolClass =
                schoolClassRepository.findById(dto.getClassId())
                        .orElseThrow(() ->
                                new RuntimeException("Class not found"));

        Section section =
                sectionRepository.findById(dto.getSectionId())
                        .orElseThrow(() ->
                                new RuntimeException("Section not found"));

        Session session = Session.builder()
                .teacher(teacher)
                .timetable(timetable)
                .schoolClass(schoolClass)
                .section(section)
                .date(dto.getDate())
                .startTime(dto.getStartTime())
                .endTime(dto.getEndTime())
                .topic(dto.getTopic())
                .description(dto.getDescription())
                .build();

        return toDto(sessionRepository.save(session));
    }

    @Override
    public List<SessionResponseDto> getAllSessions() {
        return sessionRepository.findAll()
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    //GET SESSION + AUTO MEET
    @Override
    @Transactional
    public SessionResponseDto getSessionById(Long sessionId) {

        Session s =
                sessionRepository.findById(sessionId)
                        .orElseThrow(() ->
                                new SessionNotFoundException(
                                        "Session not found with id " + sessionId));

        if (s.getJoinLink() == null || s.getJoinLink().isEmpty()) {
            try {
                EventResponse result =
                        googleCalendarService.createEventForSession(s);

                if (result != null) {
                    s.setJoinLink(result.getJoinUrl());
                    s.setGoogleEventId(result.getEventId());
                    sessionRepository.save(s);
                }

            } catch (Exception e) {
                log.error(" MEET CREATION FAILED for session {}", sessionId, e);
            }
        }

        return toDto(s);
    }

    // NEW METHOD ADDED HERE — Fetching Session By teacher ID:-
    @Override
    public List<SessionResponseDto> getSessionsByTeacher(Long teacherId) {

        return sessionRepository
                .findByTeacher_Id(teacherId)
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    private SessionResponseDto toDto(Session s) {
        return SessionResponseDto.builder()
                .sessionId(s.getSessionId())
                .timetableId(s.getTimetable().getTimetableId())
                .classId(s.getSchoolClass().getClassId())
                .sectionId(s.getSection().getSectionId())
                .teacherId(s.getTeacher().getId())
                .date(s.getDate())
                .startTime(s.getStartTime())
                .endTime(s.getEndTime())
                .topic(s.getTopic())
                .description(s.getDescription())
                .joinLink(s.getJoinLink())
                .googleEventId(s.getGoogleEventId())
                .build();
    }
}
