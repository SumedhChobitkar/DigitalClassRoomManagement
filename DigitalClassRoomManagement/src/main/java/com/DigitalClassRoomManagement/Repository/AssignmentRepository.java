package com.DigitalClassRoomManagement.Repository;

import com.DigitalClassRoomManagement.Entity.Assignment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AssignmentRepository extends JpaRepository<Assignment, Long> {

    // Find all assignments by teacher ID
    List<Assignment> findByTeacherId(Long teacherId);

    // Find assignment by assignment ID and teacher ID
    Optional<Assignment> findByAssignmentIdAndTeacherId(Long assignmentId, Long teacherId);

    // Delete assignment by assignment ID and teacher ID
    void deleteByAssignmentIdAndTeacherId(Long assignmentId, Long teacherId);

    // Find assignments by class ID
    List<Assignment> findBySchoolClass_ClassId(Long classId);
}
