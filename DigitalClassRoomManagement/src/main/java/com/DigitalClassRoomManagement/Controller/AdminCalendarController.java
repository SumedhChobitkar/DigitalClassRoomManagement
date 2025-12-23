package com.DigitalClassRoomManagement.Controller;

import com.DigitalClassRoomManagement.Dto.EventDto;
import com.DigitalClassRoomManagement.Dto.HolidayDto;
import com.DigitalClassRoomManagement.Entity.AcademicCalendar;
import com.DigitalClassRoomManagement.Entity.Event;
import com.DigitalClassRoomManagement.Entity.Holiday;
import com.DigitalClassRoomManagement.Exception.ResourceNotFoundException;
import com.DigitalClassRoomManagement.Exception.UserNotFoundException;
import com.DigitalClassRoomManagement.Repository.AcademicCalendarRepository;
import com.DigitalClassRoomManagement.ServiceImpl.CalendarServiceImpl;
import jakarta.persistence.Id;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admincalendar")
@CrossOrigin("*")
public class AdminCalendarController {

    @Autowired
    private CalendarServiceImpl calendarService;


    @PostMapping("/createAcademicCalendar")
    public ResponseEntity<?> createAdminAcademicCalendar(@RequestBody
                                                    AcademicCalendar calendar)
    {
        try {
            calendarService.createAdminAcademicCalendar(calendar);
            return new ResponseEntity<>(calendar, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/getAcademicCalendar")
    public ResponseEntity<List<AcademicCalendar>> getAdminAcademicCalendar(){
        return new ResponseEntity<>(calendarService.getAdminAcademicCalendar(),HttpStatus.OK);
    }

    @GetMapping("/{id}/getAcademicCalendarById")
    public ResponseEntity<?> getAcademicCalendarById(@PathVariable Long id){
        try {
            AcademicCalendar calendar = calendarService.getAcademicCalendarById(id);
            return new ResponseEntity<>(calendar, HttpStatus.OK);
        } catch(ResourceNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }


    @GetMapping("/getAdminHoliday")
    public ResponseEntity<List<Holiday>> getAdminHoldiay(){
        return new ResponseEntity<>(calendarService.getAdminHoldiay(),HttpStatus.OK) ;
    }

    @GetMapping("/{id}/getAdminHolidayById")
    public ResponseEntity<?> getAdminHolidayById(@PathVariable Long id){
        try{
            return new ResponseEntity<>(calendarService.getAdminHolidayById(id),HttpStatus.OK);
        }catch(UserNotFoundException e){
            return new ResponseEntity<>(e.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/addAdminHoliday")
    public ResponseEntity<?> addAdminHoliday(@RequestBody Holiday holiday){
        try{
            return new ResponseEntity<>(calendarService.addAdminHoliday(holiday),HttpStatus.OK);
        }catch(RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/{id}/updateAdminHoliday")
    public ResponseEntity<?> updateAdminHoliday(@PathVariable Long id,@RequestBody HolidayDto holidayDto){
        try{
            return new ResponseEntity<>(calendarService.updateAdminHoliday(id,holidayDto),HttpStatus.OK);
        }catch(ResourceNotFoundException e){
            return new ResponseEntity<>(e.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("{id}/deleteAdminHoliday")
    public ResponseEntity<?> deleteAdminHoliday(@PathVariable Long id){
        try{
            return new ResponseEntity<>(calendarService.deleteAdminHoliday(id),HttpStatus.OK);
        }catch(UserNotFoundException e){
            return new ResponseEntity<>(e.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/getAdminEvent")
    public ResponseEntity<List<Event>> getAdminEvent() {
        return new ResponseEntity<>(calendarService.getAdminEvent(), HttpStatus.OK);
    }


    @GetMapping("/{id}/getAdminEventById")
    public ResponseEntity<?> getAdminEventById(@PathVariable Long id) {
        try {
            return new ResponseEntity<>(calendarService.getAdminEventById(id), HttpStatus.OK);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/addAdminEvent")
    public ResponseEntity<?> addAdminEvent(@RequestBody Event event) {
        try{
            return new ResponseEntity<>(calendarService.addAdminEvent(event), HttpStatus.OK);
        }catch(Exception e){
            return new ResponseEntity<>(e.getMessage(),HttpStatus.BAD_REQUEST);
        }

    }

    @PutMapping("/{id}/updateAdminEvent")
    public ResponseEntity<?> updateAdminEvent(@PathVariable Long id, @RequestBody EventDto eventDto) {
        try {
            return new ResponseEntity<>(calendarService.updateAdminEvent(id, eventDto), HttpStatus.OK);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{id}/deleteAdminEvent")
    public ResponseEntity<?> deleteAdminEvent(@PathVariable Long id) {
        try {
            return new ResponseEntity<>(calendarService.deleteAdminEvent(id), HttpStatus.OK);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
