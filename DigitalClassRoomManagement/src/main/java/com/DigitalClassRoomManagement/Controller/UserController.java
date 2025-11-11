package com.DigitalClassRoomManagement.Controller;

import com.DigitalClassRoomManagement.Dto.AdminResponseDTO;
import com.DigitalClassRoomManagement.Dto.userDto;
import com.DigitalClassRoomManagement.Entity.UnAppproveAdmins;
import com.DigitalClassRoomManagement.Entity.User;
import com.DigitalClassRoomManagement.Exception.UserNotFoundException;
import com.DigitalClassRoomManagement.Service.UserService;
import com.DigitalClassRoomManagement.ServiceImpl.AdminServiceImpl;
import com.DigitalClassRoomManagement.security.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/digitalClassroom")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private AdminServiceImpl adminService;

    @Autowired
    private JwtService jwtService;



    @PostMapping("/registerUser")
    public ResponseEntity<?> registeration(@RequestBody User user1)
    {
        try {
            userDto user2 = userService.registeration(user1);
            return ResponseEntity.status(HttpStatus.CREATED).body(user2);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Email Already Exist");
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestParam String email,@RequestParam String password)
    {
        try {
            User user = userService.login(email, password);

            String token = jwtService.generateToken(user.getEmail(), user.getRole());

            Map<String, Object> response = new HashMap<>();
            response.put("message", "Login Successful");
            response.put("token", token);

            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        catch (UserNotFoundException e)
        {
            return ResponseEntity.badRequest().body("Password not Match");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Email Not Exist");
        }
    }

    @GetMapping("/getAll")
    public ResponseEntity<?> getAllUser()
    {
        try {
            List<userDto> u = userService.getAll();
            return ResponseEntity.ok(u);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("DATA NOT FOUND");
        }
    }

    @GetMapping("/getAllUnapprovedAdmins")
    public List<AdminResponseDTO> getAllUnApproveAdmins(){
        return adminService.getAllUnApproveAdmins();
    }

    @GetMapping("/makeAdmin/{id}")
    public String makeAdmin(@PathVariable Long id){
        return adminService.makeAdmin(id);
    }

    @GetMapping("/getById/{id}")
    public ResponseEntity<?> getUserById(@PathVariable Long id)
    {
        try {
            userDto u = userService.getUserById(id);
            return ResponseEntity.ok(u);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("ID NOT FOUND");
        }
    }
}
