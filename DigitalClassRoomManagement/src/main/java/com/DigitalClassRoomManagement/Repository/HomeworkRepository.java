package com.DigitalClassRoomManagement.Repository;

import com.DigitalClassRoomManagement.Entity.homework;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HomeworkRepository extends JpaRepository<homework, Long> {
}
