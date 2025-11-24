package com.DigitalClassRoomManagement.ServiceImpl;

import com.DigitalClassRoomManagement.Entity.Notification;
import com.DigitalClassRoomManagement.Exception.ResourceNotFoundException;
import com.DigitalClassRoomManagement.Exception.UserNotFoundException;
import com.DigitalClassRoomManagement.Repository.NotificationRepository;
import com.DigitalClassRoomManagement.Service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class NotificationImpl implements NotificationService {

    @Autowired
    private NotificationRepository notificationRepo;

    public Notification addNotification(Notification notification){
       try {
           notificationRepo.save(notification);
           return notification;
       }catch(RuntimeException e){
           throw new RuntimeException("Something went wrong");
       }
    }

    @Override
    public Notification getById(Long id){
        return notificationRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Not found"));
    }

    @Override
    public List<Notification> getAll(){
        return notificationRepo.findAll();
    }
    @Override
    public Notification updateNotification(Long id, Notification updated) {

        Notification existing = notificationRepo.findById(id)
                .orElseThrow(() -> new UserNotFoundException("Notification not found with ID: " + id));

        if (updated.getTypeOf() != null) {
            existing.setTypeOf(updated.getTypeOf());
        }
        if (updated.getNotification() != null) {
            existing.setNotification(updated.getNotification());
        }
        if (updated.getEventName() != null) {
            existing.setEventName(updated.getEventName());
        }
        if (updated.getAssignment() != null) {
            existing.setAssignment(updated.getAssignment());
        }

        return notificationRepo.save(existing);
    }

    @Override
    public String delete(Long id){
        Optional<Notification> n1 = notificationRepo.findById(id);
        if(!n1.isPresent()){
            throw new UserNotFoundException("Cannot find user with the ID");
        }
        notificationRepo.deleteById(id);
        return "The Notification Deleted for the given ID";
    }

    @Override
    public Notification getNotificationById(Long id) throws ResourceNotFoundException {
        Optional<Notification> n1 = notificationRepo.findById(id);
        if(n1.isPresent()){
            return n1.get();
        }
        throw new ResourceNotFoundException("Cannot find the notification with the given ID");
    }
}