package com.DigitalClassRoomManagement.Controller;

import com.DigitalClassRoomManagement.Dto.UserDto;
import com.DigitalClassRoomManagement.Entity.User;
import com.DigitalClassRoomManagement.Exception.UserNotFoundException;
import com.DigitalClassRoomManagement.Service.UserService;
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
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtService jwtService;

    @PostMapping("/registerUser")
    public ResponseEntity<?> registeration(@RequestBody User user1)
    {
        try {
            UserDto user2 = userService.registeration(user1);
            return ResponseEntity.status(HttpStatus.CREATED).body(user2);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Email Already Exist Or "+e.getMessage());
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
            return ResponseEntity.badRequest().body("Password not Match Or "+e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Email Not Exist Or "+e.getMessage());
        }
    }

    @GetMapping("/getAll")
    public ResponseEntity<?> getAllUser()
    {
        try {
            List<UserDto> u = userService.getAll();
            return ResponseEntity.ok(u);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("DATA NOT FOUND Or"+e.getMessage());
        }
    }

    @GetMapping("/getById/{id}")
    public ResponseEntity<?> getUserById(@PathVariable Long id)
    {
        try {
            UserDto u = userService.getUserById(id);
            return ResponseEntity.ok(u);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("ID NOT FOUND Or "+e.getMessage());
        }
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestParam String email) {
        try {
            String result = userService.forgotPassword(email);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<?> verifyOtp(@RequestParam String email, @RequestParam String otp) {
        try {
            boolean isValid = userService.verifyOtp(email, otp);
            if (isValid) {
                return ResponseEntity.ok("OTP verified! You can reset your password.");
            } else {
                return ResponseEntity.badRequest().body("Invalid or expired OTP.");
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(
            @RequestParam String email,
            @RequestParam String newPassword,
            @RequestParam String confirmPassword) {
        try {
            String result = userService.resetPassword(email, newPassword, confirmPassword);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    @PutMapping("/logout/{id}")
    public ResponseEntity<?> logout(@PathVariable Long id) {
        try {
            String msg = userService.logout(id);
            return ResponseEntity.ok(msg);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

}
