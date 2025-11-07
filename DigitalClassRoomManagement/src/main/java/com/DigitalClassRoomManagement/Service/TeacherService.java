package com.DigitalClassRoomManagement.Service;

import com.DigitalClassRoomManagement.Dto.TeacherDto;
import com.DigitalClassRoomManagement.Entity.Teacher;

import java.util.List;

public interface TeacherService {
    public String addTeacher(TeacherDto dto);
    public List<Teacher> getAllTeacher();
    public Teacher getTeacherById(Long id);
    public String updateTeacherInfo(Long id ,TeacherDto dto);
    public String deleteTeacherById(Long id);
}
