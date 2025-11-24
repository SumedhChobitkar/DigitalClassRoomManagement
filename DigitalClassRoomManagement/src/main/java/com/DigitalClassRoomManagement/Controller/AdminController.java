package com.DigitalClassRoomManagement.Controller;

import com.DigitalClassRoomManagement.Entity.Admin;
import com.DigitalClassRoomManagement.Entity.User;
import com.DigitalClassRoomManagement.Enum.Status;
import com.DigitalClassRoomManagement.Service.AdminService;
import com.DigitalClassRoomManagement.Dto.AdminDTO;
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

    @GetMapping("/get/unapproved/statusrequest")
    public ResponseEntity<List<?>>getUnapprovedStatusRequest()
    {
        try
        {
            return ResponseEntity.ok(adminService.getUnapprovedStatusRequest());
        } catch (Exception e) {
            throw e;
        }
    }

    @GetMapping("/get/approved/statusrequest")
    public ResponseEntity<List<?>>getapprovedStatusRequest()
    {
        try
        {
            return ResponseEntity.ok(adminService.getapprovedStatusRequest());
        } catch (Exception e) {
            throw e;
        }
    }

    @PutMapping("/update/status/{id}")
    public ResponseEntity<?> updateStatus(@PathVariable Long id,@RequestParam Status status)
    {
        try
        {
            User ad=adminService.updateStatus(id,status);
            return ResponseEntity.status(HttpStatus.OK).body("Status Changed");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Not Changed");
        }
    }
}
