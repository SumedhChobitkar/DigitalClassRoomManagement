package com.DigitalClassRoomManagement.Controller;

import com.DigitalClassRoomManagement.Entity.Notification;
import com.DigitalClassRoomManagement.Exception.UserNotFoundException;
import com.DigitalClassRoomManagement.ServiceImpl.NotificationImpl;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/notification")
public class NotificationController {


     @Autowired
     private NotificationImpl notificationImpl;

     private static final Logger logger = LoggerFactory.getLogger(NotificationController.class);

    @PostMapping("/addNotification")
    public Notification addNotification(@RequestBody Notification notification){
      try {
          logger.info("Posted a notification successfully");
          notificationImpl.addNotification(notification);
          return notification;
      }catch(RuntimeException e){
          throw new RuntimeException("Something went wrong");
      }
    }

    @GetMapping("/getAllNotification")
    public List<Notification> getAll() {
        logger.info("SuccessFully returned all the notification from the database");
        return notificationImpl.getAll();
    }

    @GetMapping("/{id}/getNotificationById")
    public ResponseEntity<?> getById(@PathVariable  Long id) {
        Notification n1= null;
        try{
           n1= notificationImpl.getById(id);
        }catch(UserNotFoundException e){
            logger.warn("Cannot find the notificarion with the given ID");
            return new ResponseEntity<>("Cannot find the notification with the given ID",HttpStatus.BAD_REQUEST);
        }
        logger.info("SuccessFully returned a notification with its id");
        return new ResponseEntity<>(n1,HttpStatus.OK);
    }

    @PutMapping("/{id}/updateNotification")
    public ResponseEntity<?> updateNotification(@PathVariable Long id,
                                                @RequestBody Notification updatedData) {
        try {
            Notification updated = notificationImpl.updateNotification(id, updatedData);
            return new ResponseEntity<>(updated, HttpStatus.OK);

        } catch (UserNotFoundException e) {
            logger.warn("Cannot update, notification not found with ID {}", id);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}/deleteNotification")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        try{
            notificationImpl.delete(id);
        }catch(UserNotFoundException e){
            logger.warn("Cannot find the notification with the given ID");
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
        logger.info("SuccessFUlly deleted a notification with the given ID");
        return new ResponseEntity<>("Notification deleted with the given ID",HttpStatus.OK);
    }
}
