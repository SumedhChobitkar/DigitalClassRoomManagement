package com.DigitalClassRoomManagement.Repository;

import com.DigitalClassRoomManagement.Entity.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeacherRepository extends JpaRepository<Teacher,Long> {
}
