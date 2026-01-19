package com.DigitalClassRoomManagement.Controller;


import com.DigitalClassRoomManagement.Dto.TeacherDto;
import com.DigitalClassRoomManagement.Entity.Teacher;
import com.DigitalClassRoomManagement.Exception.InvalidImageFormatException;
import com.DigitalClassRoomManagement.Exception.TeacherNotFoundException;
import com.DigitalClassRoomManagement.Service.TeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
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
    public ResponseEntity<byte[]> getProfilePicture(@PathVariable Long id) {

        byte[] image = teacherService.getProfilePicture(id);

        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG)
                .body(image);
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
