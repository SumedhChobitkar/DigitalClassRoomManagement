package com.DigitalClassRoomManagement.Repository;

import com.DigitalClassRoomManagement.Entity.Teacher;
import com.DigitalClassRoomManagement.Enum.TeacherStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TeacherRepository extends JpaRepository<Teacher,Long> {
    boolean existsByEmail(String email);
    boolean existsByPhone(String phone);
    List<Teacher> findByStatus(TeacherStatus status);
}
