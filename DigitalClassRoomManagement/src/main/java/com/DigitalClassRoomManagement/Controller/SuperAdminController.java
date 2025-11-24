package com.DigitalClassRoomManagement.Controller;

import com.DigitalClassRoomManagement.Entity.User;
import com.DigitalClassRoomManagement.Enum.Status;
import com.DigitalClassRoomManagement.Service.SuperAdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/superAdmin")
public class SuperAdminController {

    @Autowired
    private SuperAdminService superAdminService;

    @GetMapping("/get/unapproved/statusrequest")
    public ResponseEntity<List<?>>getunApprovedStatusRequest()
    {
        try
        {
            return ResponseEntity.ok(superAdminService.getunApprovedStatusRequest());
        } catch (Exception e) {
              throw e;
        }
    }
    @GetMapping("/get/approved/statusrequest")
    public ResponseEntity<List<?>>getApprovedStatusRequest()
    {
        try
        {
            return ResponseEntity.ok(superAdminService.getApprovedStatusRequest());
        } catch (Exception e) {
            throw e;
        }
    }

    @PutMapping("/update/status/{id}")
    public ResponseEntity<?> updateStatus(@PathVariable Long id,@RequestParam Status status)
    {
        try
        {
            User ad=superAdminService.updateStatus(id,status);
            return ResponseEntity.status(HttpStatus.OK).body("Status Changed");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Not Changed"+e.getMessage());
        }
    }
}
