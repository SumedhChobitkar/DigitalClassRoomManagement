package com.DigitalClassRoomManagement.Repository;

import com.DigitalClassRoomManagement.Entity.ExamQuestion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExamQuestionRepository extends JpaRepository<ExamQuestion, Long> {

    List<ExamQuestion> findByExam_TeacherId(Long teacherId);

}
