// file: com/DigitalClassRoomManagement/Service/TeacherService.java
package com.DigitalClassRoomManagement.Service;

import com.DigitalClassRoomManagement.Dto.LeaveRequestDto;
import com.DigitalClassRoomManagement.Dto.AssignTeacherRequestDto;
import com.DigitalClassRoomManagement.Dto.SchoolClassResponseDto;
import com.DigitalClassRoomManagement.Dto.TeacherDto;
import com.DigitalClassRoomManagement.Dto.TeacherResponseDto;
import com.DigitalClassRoomManagement.Entity.LeaveRequest;
import com.DigitalClassRoomManagement.Entity.Teacher;
import com.DigitalClassRoomManagement.Entity.User;
import com.DigitalClassRoomManagement.Enum.Status;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface TeacherService {

    // CREATE
    String addTeacher(TeacherDto dto);

    // READ - entities (if you still need raw entities elsewhere)
    List<Teacher> getAllTeacher();

    // READ (DTO) – safer for controllers / JSON serialization (prevents LazyInitializationException)
    List<TeacherResponseDto> getAllTeacherDtos();

    // READ single entity
    Teacher getTeacherById(Long id);

    // UPDATE / DELETE
    String updateTeacherInfo(Long id, TeacherDto dto);
    String deleteTeacherById(Long id);

    // MAPPING: Assign / Unassign classes to teacher
    String assignClassToTeacher(Long teacherId, Long classId);
    String unassignClassFromTeacher(Long teacherId, Long classId);

    // LOOKUPS: For SchoolClassController and TeacherController
    List<SchoolClassResponseDto> getClassesOfTeacher(Long teacherId);

    /**
     * IMPORTANT: This method now returns DTOs (not Entity objects).
     * Controller (SchoolClassController#getTeachersOfClass) expects DTOs to serialize safely.
     */
    List<TeacherResponseDto> getTeachersOfClass(Long classId);
    // LeaveRequest applyForLeave(LeaveRequest leaveRequest);
    LeaveRequest applyForLeave(LeaveRequestDto dto);
    List<LeaveRequest> viewStudentPendingLeaveRequests();
    //getleaveby teacherid
    List<LeaveRequest> getLeaveByTeacherId(Long teacherId);
    //getAllTeacher
    List<LeaveRequest> getAllTeacherLeaves();

    LeaveRequest approveStudentLeaveRequest(Long leaveRequestId);

    LeaveRequest rejectStudentLeaveRequest(Long leaveRequestId, String remarks);

    public List<User> getUnapprovedStatusRequest();
    public List<User> getapprovedStatusRequest();
    public User updateStatus(Long id, Status status);

    String assignTeacher(Long classId, Long sectionId, AssignTeacherRequestDto dto);
    List<TeacherDto> getTeacherByClassId(Long classId);
    List<TeacherDto> getTeacherBySectionId(Long sectionId);

    //Teacher dashboard profile


    void uploadProfilePicture(Long id, MultipartFile file) throws IOException;

    void updateProfilePicture(Long id, MultipartFile file) throws IOException;
    void deleteProfilePicture(Long id);
    byte[] getProfilePicture(Long id);
}
