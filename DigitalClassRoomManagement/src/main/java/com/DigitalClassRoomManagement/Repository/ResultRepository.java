package com.DigitalClassRoomManagement.Repository;

import com.DigitalClassRoomManagement.Entity.Result;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ResultRepository extends JpaRepository<Result,Long> {

    List<Result> findByStudentStudentRegId(Long studentId);

    List<Result> findByExamExamId(Long examId);

    List<Result> findByStudentStudentRegIdAndExamExamId(Long studentId, Long examId);

    List<Result> findAllByOrderByObtainedMarksDesc();

}
