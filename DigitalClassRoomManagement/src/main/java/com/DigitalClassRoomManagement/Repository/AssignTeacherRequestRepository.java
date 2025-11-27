package com.DigitalClassRoomManagement.Repository;

import com.DigitalClassRoomManagement.Entity.AssignTeacherRequest;
import com.DigitalClassRoomManagement.Entity.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AssignTeacherRequestRepository extends JpaRepository<AssignTeacherRequest,Long> {
    @Query("SELECT a.teacher FROM AssignTeacherRequest a WHERE a.schoolClass.id = :classId")
    List<Teacher> findTeachersByClassId(Long classId);

    @Query("SELECT a.teacher FROM AssignTeacherRequest a WHERE a.sectionId.id = :sectionId")
    List<Teacher> findTeachersBySectionId(Long sectionId);



}
