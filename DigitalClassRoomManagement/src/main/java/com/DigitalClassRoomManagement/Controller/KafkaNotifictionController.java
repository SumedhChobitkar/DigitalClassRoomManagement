package com.DigitalClassRoomManagement.Controller;

import com.DigitalClassRoomManagement.Entity.KafkaNotification;
import com.DigitalClassRoomManagement.ServiceImpl.KafkaNotificationServiceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
public class KafkaNotifictionController{
    @Autowired
    private  KafkaNotificationServiceImpl kafkaService;

    @GetMapping("/getAll/{userId}")
    public List<KafkaNotification> getNotifications(@PathVariable String userId) {
        return kafkaService.getNotificationsForYesterdayAndToday(userId);
    }
}
