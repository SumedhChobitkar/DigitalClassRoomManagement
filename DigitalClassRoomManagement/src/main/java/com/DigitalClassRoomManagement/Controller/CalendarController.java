package com.DigitalClassRoomManagement.Controller;


import com.DigitalClassRoomManagement.Repository.AdminRepository;
import com.DigitalClassRoomManagement.Service.CalendarService;
import com.DigitalClassRoomManagement.Dto.*;
import com.DigitalClassRoomManagement.Entity.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/calendars")
@RequiredArgsConstructor
@CrossOrigin("*")
public class CalendarController {

    private final CalendarService calendarService;

    @Autowired
    private AdminRepository adminrepo;

    @PostMapping("/create/{id}")
    public ResponseEntity<com.DigitalClassRoomManagement.Dto.CalendarDto> createCalendar(
            @RequestBody com.DigitalClassRoomManagement.Dto.CreateAcademicCalenderRequest request,
            @PathVariable Long id) {
        Optional<Admin> dummyAdmin = adminrepo.findById(id);
        if(!dummyAdmin.isPresent()){
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        }
        String username = dummyAdmin.get().getFirstName();
        return ResponseEntity.ok(calendarService.createAcademicCalender(request, username));
    }

    @PostMapping("/{calendarId}/holiday")
    public ResponseEntity<com.DigitalClassRoomManagement.Dto.HolidayDto> addHoliday(@PathVariable Long calendarId, @RequestBody Holiday holiday) {
        return ResponseEntity.ok(calendarService.addHoliday(calendarId, holiday));
    }

    @PostMapping("/{calendarId}/event")
    public ResponseEntity<EventDto> addEvent(@PathVariable Long calendarId, @RequestBody Event event) {
        return ResponseEntity.ok(calendarService.addEvent(calendarId, event));
    }

    @GetMapping("/events")
    public ResponseEntity<List<EventDto>> viewEvents() {
        return ResponseEntity.ok(calendarService.viewCalenderEvents());
    }

    @GetMapping("/{calendarId}")
    public ResponseEntity<CalendarDto> getCalendar(@PathVariable Long calendarId) {
        return ResponseEntity.ok(calendarService.getAcademicCalender(calendarId));
    }

    @PutMapping("/{calendarId}")
    public ResponseEntity<CalendarDto> updateCalendar(
            @PathVariable Long calendarId,
            @RequestBody com.DigitalClassRoomManagement.Dto.UpdateAcademicCalenderRequest request) {
        return ResponseEntity.ok(calendarService.updateAcademicCalender(calendarId, request));
    }

    @GetMapping
    public ResponseEntity<List<com.DigitalClassRoomManagement.Dto.CalendarDto>> getAllCalendars() {
        return ResponseEntity.ok(calendarService.getAcademicCalenders());
    }

    @DeleteMapping("/holiday/{holidayId}")
    public ResponseEntity<String> deleteHoliday(@PathVariable Long holidayId) {
        return ResponseEntity.ok(calendarService.removeHoliday(holidayId));
    }

    @DeleteMapping("/event/{eventId}")
    public ResponseEntity<String> deleteEvent(@PathVariable Long eventId) {
        return ResponseEntity.ok(calendarService.removeEvent(eventId));
    }
}