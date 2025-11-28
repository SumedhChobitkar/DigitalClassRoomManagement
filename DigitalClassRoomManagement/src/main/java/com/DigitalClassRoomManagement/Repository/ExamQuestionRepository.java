package com.DigitalClassRoomManagement.Repository;

import com.DigitalClassRoomManagement.Entity.ExamQuestion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExamQuestionRepository extends JpaRepository<ExamQuestion, Long> {
}
