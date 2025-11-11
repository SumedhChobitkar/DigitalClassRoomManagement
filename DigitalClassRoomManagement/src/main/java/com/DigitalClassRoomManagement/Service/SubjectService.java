package com.DigitalClassRoomManagement.Service;


import com.DigitalClassRoomManagement.Dto.SubjectDto;
import com.DigitalClassRoomManagement.Entity.Subject;

import java.util.List;

public interface SubjectService {
    public String addSubject(SubjectDto sdto);
    public List<Subject> getAllSubject();
    public Subject getSubjectById(Long subjectId);
    String updateSubject(SubjectDto dto, Long subjectId);
    public String deleteSubject(Long subjectId);




}
