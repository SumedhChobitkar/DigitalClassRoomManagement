package com.DigitalClassRoomManagement.Repository;

import com.DigitalClassRoomManagement.Entity.Timetable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TimetableRepository extends JpaRepository<Timetable, Long> {

    List<Timetable> findByTeacher_Id(Long teacherId);
    List<Timetable> findBySection_SectionId(Long sectionId);



}
