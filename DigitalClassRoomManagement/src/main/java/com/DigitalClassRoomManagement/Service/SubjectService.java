package com.DigitalClassRoomManagement.Service;


import com.DigitalClassRoomManagement.Dto.SubjectDto;
import com.DigitalClassRoomManagement.Entity.Subject;

import java.util.List;

public interface SubjectService {
    String addSubject(SubjectDto sdto);
    List<Subject> getAllSubject();
    Subject getSubjectById(Long subjectId);
    String updateSubject(SubjectDto dto, Long subjectId);
    String deleteSubject(Long subjectId);

    List<SubjectDto> getSubjectsForStudent(Long studentRegId);

    SubjectDto assignSubjectToClass(Long subjectId, Long classId);



}