package com.DigitalClassRoomManagement.Repository;

import com.DigitalClassRoomManagement.Entity.Exam;
import com.DigitalClassRoomManagement.Entity.ExamSubmission;
import com.DigitalClassRoomManagement.Entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExamSubmissionRepository extends JpaRepository<ExamSubmission, Long> {

    List<ExamSubmission> findByExam(Exam exam);
    boolean existsByExamAndStudent(Exam exam, Student student);


}
