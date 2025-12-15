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

@RequestMapping("/api/teacherCalendar")
@RestController
@CrossOrigin(origins = "*")
public class TeacherCalendarController {

    @Autowired
    private CalendarServiceImpl calendarService;

    @GetMapping("/viewTeacherCalendarHoliday")
    public ResponseEntity<List<Holiday>> viewStudentCalendarHoliday(){
        return new ResponseEntity<>(calendarService.viewTeacherCalendarHoliday(), HttpStatus.OK);
    }

    @GetMapping("/viewTeacherCalendarEvents")
    public ResponseEntity<List<Event>> viewStudentCalendarEvents(){
        return new ResponseEntity<>(calendarService.viewTeacherCalendarEvents(),HttpStatus.OK);
    }
}
