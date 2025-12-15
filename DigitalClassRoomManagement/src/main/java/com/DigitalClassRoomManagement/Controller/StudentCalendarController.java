package com.DigitalClassRoomManagement.Controller;

import com.DigitalClassRoomManagement.Entity.Event;
import com.DigitalClassRoomManagement.Entity.Holiday;
import com.DigitalClassRoomManagement.ServiceImpl.CalendarServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/studentCalendar")
@CrossOrigin(origins = "*")
public class StudentCalendarController {

    @Autowired
    private CalendarServiceImpl calendarService;

    @GetMapping("/viewStudentCalendarHoliday")
    public ResponseEntity<List<Holiday>> viewStudentCalendarHoliday(){
        return new ResponseEntity<>(calendarService.viewStudentCalendarHoliday(), HttpStatus.OK);
    }

    @GetMapping("/viewStudentCalendarEvents")
    public ResponseEntity<List<Event>> viewStudentCalendarEvents(){
        return new ResponseEntity<>(calendarService.viewStudentCalendarEvents(),HttpStatus.OK);
    }
}
