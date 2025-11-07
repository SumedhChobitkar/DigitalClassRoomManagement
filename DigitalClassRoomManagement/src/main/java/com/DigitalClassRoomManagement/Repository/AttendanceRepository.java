package com.DigitalClassRoomManagement.Repository;

import com.DigitalClassRoomManagement.Entity.Attendance;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AttendanceRepository extends JpaRepository<Attendance,Long> {
}
