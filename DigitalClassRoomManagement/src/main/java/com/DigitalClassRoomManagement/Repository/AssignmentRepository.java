package com.DigitalClassRoomManagement.Repository;

import com.DigitalClassRoomManagement.Entity.Assignment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AssignmentRepository extends JpaRepository<Assignment, Long> {

    List<Assignment> findByTeacherId(Long teacherId);

    Optional<Assignment> findByAssignmentIdAndTeacherId(Long assignmentId, Long teacherId);
    void deleteByAssignmentIdAndTeacherId(Long assignmentId, Long teacherId);


}
