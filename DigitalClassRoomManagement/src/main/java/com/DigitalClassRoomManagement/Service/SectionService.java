package com.DigitalClassRoomManagement.Service;


import com.DigitalClassRoomManagement.Dto.SectionDTO;
import com.DigitalClassRoomManagement.Entity.Section;

import java.util.List;

public interface SectionService {

    Section createSection(SectionDTO sectionDTO);
    List<Section> getAllSections();
    Section getSectionById(Long id);
    Section updateSection(Long id, SectionDTO sectionDTO);
    boolean deleteSection(Long id);
    List<Section> findSectionsByTeacherId(Long teacherId);
    //List<Section> getSectionsByTeacherId(Long teacherId);
    List<Section> getSectionsByClassName(String className);
    Section getSectionBySectionId(Long sectionId);




}
