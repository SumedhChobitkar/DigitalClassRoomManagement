package com.DigitalClassRoomManagement.Repository;

import com.DigitalClassRoomManagement.Entity.Timetable;
import com.DigitalClassRoomManagement.Enum.DayOfWeek;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TimetableRepository extends JpaRepository<Timetable, Long> {

    List<Timetable> findByTeacher_Id(Long teacherId);
    List<Timetable> findBySection_SectionId(Long sectionId);
    List<Timetable> findByTeacher_IdAndDayOfWeek(Long id, DayOfWeek dayOfWeek);

}
