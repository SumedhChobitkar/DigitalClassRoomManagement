package com.DigitalClassRoomManagement.Repository;

import com.DigitalClassRoomManagement.Entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationRepository extends JpaRepository<Notification,Long> {
}
