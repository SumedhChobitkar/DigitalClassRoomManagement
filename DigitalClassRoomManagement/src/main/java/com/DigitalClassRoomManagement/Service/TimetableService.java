package com.DigitalClassRoomManagement.Service;

import com.DigitalClassRoomManagement.Dto.TimetableDTO;
import com.DigitalClassRoomManagement.Entity.Timetable;

import java.util.List;

public interface TimetableService {

    Timetable createTimetable(TimetableDTO dto);

    Timetable updateTimetable(Long id, TimetableDTO dto);

    Timetable getTimetableById(Long id);

    List<Timetable> getAllTimetables();

    List<Timetable> getTimetableByTeacherId(Long teacherId);

    List<Timetable> getTimetableBySectionId(Long sectionId);


    String deleteTimetable(Long id);
}
