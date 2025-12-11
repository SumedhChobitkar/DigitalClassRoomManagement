package com.DigitalClassRoomManagement.Controller;

import com.DigitalClassRoomManagement.Exception.ResourceNotFoundException;
import com.DigitalClassRoomManagement.ServiceImpl.NotificationImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/holidayEventNotification")
@RestController
@CrossOrigin(origins = "*")
public class HolidayEventNotificationController {

    @Autowired
    private NotificationImpl notification;

    @GetMapping("/{id}/getNotificationById")
    public ResponseEntity<?> getNotificationById(@PathVariable Long id){
        try{
            return new ResponseEntity<>(notification.getNotificationById(id), HttpStatus.OK);
        }catch(ResourceNotFoundException e){
             return new ResponseEntity<>(e.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }
}
