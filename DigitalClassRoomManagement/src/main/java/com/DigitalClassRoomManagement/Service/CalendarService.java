package com.DigitalClassRoomManagement.Service;

import com.DigitalClassRoomManagement.dto.*;
import com.DigitalClassRoomManagement.Entity.Event;
import com.DigitalClassRoomManagement.Entity.Holiday;

import java.util.List;

public interface CalendarService {

    CalendarDto createAcademicCalender(CreateAcademicCalenderRequest request, String username);

    HolidayDto addHoliday(Long calendarId, Holiday holiday);
    HolidayDto updateHoliday(Long holidayId, Holiday holiday);
    String removeHoliday(Long holidayId);
    HolidayDto viewHoliday(Long holidayId);
    List<HolidayDto> viewHolidays();

    EventDto addEvent(Long calendarId, Event event);
    EventDto updateEvent(Long eventId, Event event);
    String removeEvent(Long eventId);
    EventDto viewEvent(Long eventId);
    List<EventDto> viewCalenderEvents();

    CalendarDto updateAcademicCalender(Long calendarId, UpdateAcademicCalenderRequest request);
    List<CalendarDto> getAcademicCalenders();
    CalendarDto getAcademicCalender(Long calendarId);
}
