package com.DigitalClassRoomManagement.Service;

import com.DigitalClassRoomManagement.Entity.Notification;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

public interface NotificationService {
    public Notification addNotification(Notification notification);
    public Notification getById(Long id);
    public List<Notification> getAll();
    public Notification updateNotification(Long id, Notification updated);
    public String delete(Long id);

    public Notification getNotificationById(Long id);
}
