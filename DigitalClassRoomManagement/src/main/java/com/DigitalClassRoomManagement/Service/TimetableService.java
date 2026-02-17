package com.DigitalClassRoomManagement.Service;

import com.DigitalClassRoomManagement.Dto.TimetableDTO;
import com.DigitalClassRoomManagement.Entity.Timetable;

import java.util.HashMap;
import java.util.List;

public interface TimetableService {

    Timetable createTimetable(TimetableDTO dto);

    Timetable updateTimetable(Long id, TimetableDTO dto);

    Timetable getTimetableById(Long id);

   // List<Timetable> getAllTimetables();
   //List<TimetableDTO> getAllTimetables();
   List<HashMap<String, Object>> getAllTimetables();


   // List<Timetable> getTimetableByTeacherId(Long teacherId);
   List<HashMap<String, Object>> getTimetableByTeacherId(Long teacherId);


    //    List<Timetable> getTimetableBySectionId(Long sectionId);
List<HashMap<String, Object>> getTimetableBySectionId(Long sectionId);



    String deleteTimetable(Long id);
}
