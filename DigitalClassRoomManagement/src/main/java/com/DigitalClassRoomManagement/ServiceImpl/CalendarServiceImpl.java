package com.DigitalClassRoomManagement.ServiceImpl;


import com.DigitalClassRoomManagement.dto.*;
import com.DigitalClassRoomManagement.Entity.*;
import com.DigitalClassRoomManagement.Repository.*;
import com.DigitalClassRoomManagement.Service.CalendarService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CalendarServiceImpl implements CalendarService {

    private final AcademicCalendarRepository calendarRepo;
    private final EventRepository eventRepo;
    private final HolidayRepository holidayRepo;

    @Override
    public CalendarDto createAcademicCalender(CreateAcademicCalenderRequest request, String username) {
        AcademicCalendar calendar = AcademicCalendar.builder()
                .academicYear(request.getAcademicYear())
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .createdBy(username)
                .build();
        calendarRepo.save(calendar);
        return mapToDto(calendar);
    }

    @Override
    public HolidayDto addHoliday(Long calendarId, Holiday holiday) {
        AcademicCalendar calendar = calendarRepo.findById(calendarId).orElseThrow();
        holiday.setCalendar(calendar);
        holidayRepo.save(holiday);
        return mapHolidayToDto(holiday);
    }

    @Override
    public EventDto addEvent(Long calendarId, Event event) {
        AcademicCalendar calendar = calendarRepo.findById(calendarId).orElseThrow();
        event.setCalendar(calendar);
        eventRepo.save(event);
        return mapEventToDto(event);
    }

    @Override
    public List<EventDto> viewCalenderEvents() {
        return eventRepo.findAll().stream().map(this::mapEventToDto).collect(Collectors.toList());
    }

    @Override
    public CalendarDto updateAcademicCalender(Long calendarId, UpdateAcademicCalenderRequest request) {
        AcademicCalendar calendar = calendarRepo.findById(calendarId).orElseThrow();
        calendar.setAcademicYear(request.getAcademicYear());
        calendar.setStartDate(request.getStartDate());
        calendar.setEndDate(request.getEndDate());
        calendarRepo.save(calendar);
        return mapToDto(calendar);
    }

    @Override
    public List<CalendarDto> getAcademicCalenders() {
        return calendarRepo.findAll().stream().map(this::mapToDto).collect(Collectors.toList());
    }

    @Override
    public CalendarDto getAcademicCalender(Long calendarId) {
        return mapToDto(calendarRepo.findById(calendarId).orElseThrow());
    }

    @Override
    public HolidayDto updateHoliday(Long holidayId, Holiday holiday) {
        Holiday existing = holidayRepo.findById(holidayId).orElseThrow();
        existing.setHolidayName(holiday.getHolidayName());
        existing.setDescription(holiday.getDescription());
        existing.setHolidayDate(holiday.getHolidayDate());
        holidayRepo.save(existing);
        return mapHolidayToDto(existing);
    }

    @Override
    public String removeHoliday(Long holidayId) {
        holidayRepo.deleteById(holidayId);
        return "Holiday removed successfully";
    }

    @Override
    public String removeEvent(Long eventId) {
        eventRepo.deleteById(eventId);
        return "Event removed successfully";
    }

    @Override
    public EventDto updateEvent(Long eventId, Event event) {
        Event existing = eventRepo.findById(eventId).orElseThrow();
        existing.setEventName(event.getEventName());
        existing.setDescription(event.getDescription());
        existing.setEventDate(event.getEventDate());
        eventRepo.save(existing);
        return mapEventToDto(existing);
    }

    @Override
    public EventDto viewEvent(Long eventId) {
        return mapEventToDto(eventRepo.findById(eventId).orElseThrow());
    }

    @Override
    public HolidayDto viewHoliday(Long holidayId) {
        return mapHolidayToDto(holidayRepo.findById(holidayId).orElseThrow());
    }

    @Override
    public List<HolidayDto> viewHolidays() {
        return holidayRepo.findAll().stream().map(this::mapHolidayToDto).collect(Collectors.toList());
    }

    private CalendarDto mapToDto(AcademicCalendar cal) {
        return CalendarDto.builder()
                .id(cal.getId())
                .academicYear(cal.getAcademicYear())
                .startDate(cal.getStartDate())
                .endDate(cal.getEndDate())
                .createdBy(cal.getCreatedBy())
                .holidays(cal.getHolidays() == null ? null :
                        cal.getHolidays().stream().map(this::mapHolidayToDto).collect(Collectors.toList()))
                .events(cal.getEvents() == null ? null :
                        cal.getEvents().stream().map(this::mapEventToDto).collect(Collectors.toList()))
                .build();
    }

    private EventDto mapEventToDto(Event e) {
        return EventDto.builder()
                .id(e.getId())
                .eventName(e.getEventName())
                .description(e.getDescription())
                .eventDate(e.getEventDate())
                .build();
    }

    private HolidayDto mapHolidayToDto(Holiday h) {
        return HolidayDto.builder()
                .id(h.getId())
                .holidayName(h.getHolidayName())
                .holidayDate(h.getHolidayDate())
                .description(h.getDescription())
                .build();
    }
}
