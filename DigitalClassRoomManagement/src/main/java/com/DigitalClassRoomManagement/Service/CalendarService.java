package com.DigitalClassRoomManagement.Service;

import com.DigitalClassRoomManagement.Dto.*;
import com.DigitalClassRoomManagement.Entity.AcademicCalendar;
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

    public AcademicCalendar createAdminAcademicCalendar(AcademicCalendar calendar);
    public List<AcademicCalendar> getAdminAcademicCalendar();
    public AcademicCalendar getAcademicCalendarById(Long id);

    public List<Holiday> getAdminHoldiay();
    public Holiday getAdminHolidayById(Long id);
    public Holiday addAdminHoliday(Holiday holiday);
    public Holiday updateAdminHoliday(Long id,HolidayDto holiday);
    public String deleteAdminHoliday(Long id);

    List<Event> getAdminEvent();
    Event getAdminEventById(Long id);
    Event addAdminEvent(Event event);
    Event updateAdminEvent(Long id, EventDto eventDto);
    String deleteAdminEvent(Long id);

    List<Holiday> viewStudentCalendarHoliday();
    public List<Event> viewStudentCalendarEvents();

    List<Holiday>  viewTeacherCalendarHoliday();
    public List<Event> viewTeacherCalendarEvents();

}