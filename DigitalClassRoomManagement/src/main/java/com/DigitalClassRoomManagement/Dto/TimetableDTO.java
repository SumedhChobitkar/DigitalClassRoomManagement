//package com.DigitalClassRoomManagement.Dto;
//
//import com.fasterxml.jackson.annotation.JsonFormat;
//import lombok.Data;
//
//import com.DigitalClassRoomManagement.Enum.DayOfWeek;
//
//
//
//import java.time.LocalDate;
//import java.time.LocalDateTime;
//@Data
//public class TimetableDTO {
//    private Long timetableId;
//    //private String schoolClass;
//    private Long classId;
//
//    private Long sectionId;
//    private Long subjectId;
//    private Long teacherId;
//    //private DayOfWeek dayOfWeek;
////   @JsonFormat(shape = JsonFormat.Shape.STRING)
//    private DayOfWeek dayOfWeek;
//    private LocalDate date;
//    private LocalDateTime startTime;
//    private LocalDateTime endTime;
//}
package com.DigitalClassRoomManagement.Dto;

import com.DigitalClassRoomManagement.Enum.DayOfWeek;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class TimetableDTO {

    private Long timetableId;
    private Long classId;
    private Long sectionId;
    private Long subjectId;
    private Long teacherId;

    private DayOfWeek dayOfWeek; // <-- YOUR CUSTOM ENUM

    private LocalDate date;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
}

