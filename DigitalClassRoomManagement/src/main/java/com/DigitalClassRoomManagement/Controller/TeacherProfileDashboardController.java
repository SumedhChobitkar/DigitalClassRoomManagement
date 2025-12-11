package com.DigitalClassRoomManagement.Controller;


import com.DigitalClassRoomManagement.Dto.TeacherDto;
import com.DigitalClassRoomManagement.Entity.Teacher;
import com.DigitalClassRoomManagement.Exception.InvalidImageFormatException;
import com.DigitalClassRoomManagement.Exception.TeacherNotFoundException;
import com.DigitalClassRoomManagement.Service.TeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;

@RestController
@RequestMapping("/api/profile/dashboard")
@CrossOrigin(origins = "*")
public class TeacherProfileDashboardController {
    @Autowired
    private TeacherService teacherService;


    @PostMapping("/{id}/add-profile-picture")
    public ResponseEntity<String> uploadProfile(
            @PathVariable Long id,
            @RequestParam("file") MultipartFile file) {

        try {
            teacherService.uploadProfilePicture(id, file);
            return ResponseEntity.ok("Profile picture uploaded successfully!");
        } catch (InvalidImageFormatException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (TeacherNotFoundException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    @PutMapping("/{id}/update-profile-picture")
    public ResponseEntity<String> updateProfile(
            @PathVariable Long id,
            @RequestParam("file") MultipartFile file) {

        try {
            teacherService.updateProfilePicture(id, file);
            return ResponseEntity.ok("Profile picture updated successfully!");
        } catch (InvalidImageFormatException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (TeacherNotFoundException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/{id}/get-profile-picture")
    public ResponseEntity<TeacherDto> getTeacherProfile(@PathVariable Long id) {
        Teacher teacher = teacherService.getTeacherById(id);

        if (teacher == null) {
            return ResponseEntity.notFound().build();
        }

        TeacherDto dto = new TeacherDto();
        dto.setId(teacher.getId());
        dto.setFirstName(teacher.getFirstName());
        dto.setLastName(teacher.getLastName());
        dto.setEmail(teacher.getEmail());
        dto.setPhone(teacher.getPhone());
        dto.setAdminMailId(teacher.getAdminMailId());
        dto.setQualification(teacher.getQualification());
        dto.setExperienceYears(teacher.getExperienceYears());
        dto.setGender(teacher.getGender());
        dto.setDateOfBirth(teacher.getDateOfBirth());
        dto.setStatus(teacher.getStatus());





        if (teacher.getProfilePicture() != null) {
            dto.setProfilePicture(Base64.getEncoder().encodeToString(teacher.getProfilePicture()).getBytes());
        }

        return ResponseEntity.ok(dto);
    }


    @DeleteMapping("/{id}/remove-profile-picture")
    public ResponseEntity<String> deleteProfile(@PathVariable Long id) {
        try {
            teacherService.deleteProfilePicture(id);
            return ResponseEntity.ok("Profile picture deleted successfully!");
        } catch (TeacherNotFoundException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }
}
