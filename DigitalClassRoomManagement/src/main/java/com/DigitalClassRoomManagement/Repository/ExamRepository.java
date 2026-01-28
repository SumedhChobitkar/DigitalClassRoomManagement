package com.DigitalClassRoomManagement.Repository;

import com.DigitalClassRoomManagement.Entity.Exam;
import com.DigitalClassRoomManagement.Enum.ExamStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface ExamRepository extends JpaRepository<Exam,Long> {
    List<Exam> findByTeacherId(Long teacherId);

    List<Exam> findByStatus(ExamStatus status);



}
