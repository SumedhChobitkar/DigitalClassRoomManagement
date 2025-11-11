package com.DigitalClassRoomManagement.Controller;

import com.DigitalClassRoomManagement.Dto.SubjectDto;
import com.DigitalClassRoomManagement.Entity.Subject;
import com.DigitalClassRoomManagement.Service.SubjectService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/subject")
public class AdminSubjectController {
    private static  final Logger log= LoggerFactory.getLogger(AdminSubjectController.class);
    @Autowired
    private SubjectService sservice;

    @PostMapping("/add")
    public ResponseEntity<String> addNewSubject( @Valid @RequestBody SubjectDto dto){
        log.info("Recieved subject to be added with name: {}"+dto.getSubjectName());
        return ResponseEntity.ok(sservice.addSubject(dto));
    }

    @GetMapping("/fetch")
    public ResponseEntity<List<Subject>> fetchAllsubject(){
        log.info("Recieved a request to fetch all subjects");
        return ResponseEntity.ok(sservice.getAllSubject());
    }

    @GetMapping("/fetch/{subjectId}")
    public ResponseEntity<Subject> fetchSubjectById(@PathVariable Long subjectId){
        log.info("Received a request to fetch subject with id:{}", subjectId);
        return ResponseEntity.ok(sservice.getSubjectById(subjectId));
    }

    @PutMapping("/update/{subjectId}")
    public ResponseEntity<String> updateSubjectInfo(@RequestBody SubjectDto sdto,@PathVariable  Long subjectId){
        log.info("Received a request to update subject with id:{}", subjectId);
        return ResponseEntity.ok(sservice.updateSubject(sdto, subjectId));
    }

    @DeleteMapping("/delete/{subjectId}")
    public ResponseEntity<String> deleteSubject(@PathVariable Long subjectId){
        log.info("Received a request to delete subject with id:{}", subjectId);
        return ResponseEntity.ok(sservice.deleteSubject(subjectId));
    }
}
