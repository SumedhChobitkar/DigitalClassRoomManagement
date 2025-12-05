package com.DigitalClassRoomManagement.Repository;

import com.DigitalClassRoomManagement.Entity.Attendance;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AttendanceRepository extends JpaRepository<Attendance,Long> {
    Optional<Attendance> findBySessionIdAndEmail(Long sessionId, String email);

    List<Attendance> findAllBySessionId(Long sessionId);
}
