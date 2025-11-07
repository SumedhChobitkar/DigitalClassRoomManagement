package com.DigitalClassRoomManagement.Controller;

import com.DigitalClassRoomManagement.Dto.TeacherDto;
import com.DigitalClassRoomManagement.Entity.Teacher;
import com.DigitalClassRoomManagement.Service.TeacherService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/teacher")
@CrossOrigin("*")
public class TeacherController {
    @Autowired
    private TeacherService service;
    private static final Logger log= LoggerFactory.getLogger(TeacherController.class);
    @PostMapping("/add")
    public ResponseEntity<String> addTeacher(@Valid  @RequestBody TeacherDto dto){
        log.info("Received request to register new teacher");
       return ResponseEntity.ok(service.addTeacher(dto));
    }

    @GetMapping("/getAll")
    public ResponseEntity<List<Teacher>>getAllTeacher(){
        log.info("Fetching Information ");
        return ResponseEntity.ok(service.getAllTeacher());
    }

    @GetMapping("/getById/{id}")
    public ResponseEntity<Teacher>  getTeacherById(@PathVariable  Long id){
        log.info("Fetching Information for Id : "+id);
        return ResponseEntity.ok(service.getTeacherById(id));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<String> updateTeacherInfo(@PathVariable Long id, @Valid @RequestBody TeacherDto dto){
        log.info("Updating Information for Id : "+id);
        return ResponseEntity.ok(service.updateTeacherInfo(id, dto));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> updateTeacherInfo(@PathVariable Long id){
        log.info("Updating Information for Id : "+id);
        return ResponseEntity.ok(service.deleteTeacherById(id));
    }
}
