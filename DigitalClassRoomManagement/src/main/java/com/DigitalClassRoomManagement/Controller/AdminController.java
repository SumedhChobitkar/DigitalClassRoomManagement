package com.DigitalClassRoomManagement.Controller;

import com.DigitalClassRoomManagement.Entity.Admin;
import com.DigitalClassRoomManagement.Service.AdminService;
import com.DigitalClassRoomManagement.dto.AdminDTO;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;


    @PostMapping("/create")
    public ResponseEntity<String> createAdmin(@Valid @RequestBody Admin admin) {
        String message = adminService.GetData(admin);
        return new ResponseEntity<>(message, HttpStatus.CREATED);
    }


    @GetMapping("/all")
    public ResponseEntity<List<Admin>> getAllAdmins() {
        List<Admin> admins = adminService.sendData();
        return new ResponseEntity<>(admins, HttpStatus.OK);
    }


    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteById(@PathVariable Long id) {
        String message = adminService.deleteById(id);
        return new ResponseEntity<>(message, HttpStatus.OK);
    }


    @PutMapping("/update/{id}")
    public ResponseEntity<String> updateById(@PathVariable Long id, @Valid @RequestBody AdminDTO adminDTO) {
        String message = adminService.updateById(id, adminDTO);
        return new ResponseEntity<>(message, HttpStatus.OK);
    }
}
