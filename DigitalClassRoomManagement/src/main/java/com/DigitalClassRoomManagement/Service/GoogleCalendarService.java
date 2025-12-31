package com.DigitalClassRoomManagement.Service;

import com.DigitalClassRoomManagement.Dto.EventResponse;
import com.DigitalClassRoomManagement.Entity.Session;

import java.time.OffsetDateTime;
import java.util.Map;

public interface GoogleCalendarService {

    // DIRECT SESSION METHOD (required)
    EventResponse createEventForSession(Session session);

    // LOW LEVEL
    String createMeetEvent(
            Long teacherId,
            OffsetDateTime start,
            OffsetDateTime end,
            String title
    );

    // HIGH LEVEL (Controller uses this)
    Map<String, Object> createMeetEvent(Long sessionId);
}
