package com.DigitalClassRoomManagement.Dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SectionDTO {

    private Long sectionId;
    private String sectionName;
    private Integer capacity;
    //private String schoolClass;
    private Long classId;
    private List<Long> teacherIds;
}
