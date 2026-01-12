package com.DigitalClassRoomManagement.Controller;

import com.DigitalClassRoomManagement.Dto.UserDto;
import com.DigitalClassRoomManagement.Entity.User;
import com.DigitalClassRoomManagement.Enum.Role;
import com.DigitalClassRoomManagement.Exception.UserNotFoundException;
import com.DigitalClassRoomManagement.Service.UserService;
import com.DigitalClassRoomManagement.security.JwtService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "User APIs", description = "Operation on APIs for users")

public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtService jwtService;

    @Operation(summary = "Create a new user",
            description = "This API is used by admin to entry of new user.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "User created successfully",
                            content = @Content(schema = @Schema(implementation = User.class))),
                    @ApiResponse(responseCode = "500", description = "Error while creating User")
            })
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

    @Operation(summary = "Login User",
            description = "This API is used by all users to Login.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Login successfully",
                            content = @Content(schema = @Schema(implementation = User.class))),
                    @ApiResponse(responseCode = "400", description = "Email already exists"),
                    @ApiResponse(responseCode = "500", description = "Something went wrong")
            })
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestParam String email,@RequestParam String password)
    {
        try {
            User user = userService.login(email, password);

            String token = jwtService.generateToken(user.getEmail(), user.getRole());

            Map<String, Object> response = new HashMap<>();
            response.put("message", "Login Successful");
            response.put("token", token);
            response.put("userId", user.getUserId());
            response.put("role",user.getRole().name());

            response.put("studentId", null);
            response.put("teacherId", null);
            response.put("parentId", null);
            response.put("principalId", null);
            response.put("adminId", null);

            if (user.getRole() == Role.STUDENT && user.getStudent() != null) {
                response.put("studentId", user.getStudent().getStudentId());
            }

            if (user.getRole() == Role.TEACHER && user.getTeacher() != null) {
                response.put("teacherId", user.getTeacher().getId());
            }

            if (user.getRole() == Role.PARENT && user.getParent() != null) {
                response.put("parentId", user.getParent().getParentId());
            }

            if(user.getRole() == Role.PRINCIPAL && user.getAdmin() != null){
                response.put("principalId", user.getAdmin().getAdminId());
            }



            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        catch (UserNotFoundException e)
        {
            return ResponseEntity.badRequest().body("Password not Match Or "+e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Email Not Exist Or "+e.getMessage());
        }
    }

    @Operation(summary = "Get all Users",
            description = "This API is used by all users to get details.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Get Users Successfully",
                            content = @Content(schema = @Schema(implementation = User.class))),
                    @ApiResponse(responseCode = "404", description = "User Not Found"),
            })
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

    @Operation(summary = "Get User By ID",
            description = "This API is used for get user by ID.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Get User Successfully",
                            content = @Content(schema = @Schema(implementation = User.class))),
                    @ApiResponse(responseCode = "404", description = "User Not Found"),
            })
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

    @Operation(summary = "Used for Forget Password",
            description = "This API is used for changed the password.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfully forget password.",
                            content = @Content(schema = @Schema(implementation = User.class))),
                    @ApiResponse(responseCode = "400", description = "something went wrong"),
            })
    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestParam String email) {
        try {
            String result = userService.forgotPassword(email);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }


    @Operation(summary = "Used for Verify OTP",
            description = "This API is used for verify OTP.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfully Verify OTP.",
                            content = @Content(schema = @Schema(implementation = User.class))),
                    @ApiResponse(responseCode = "400", description = "something went wrong"),
            })
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

    @Operation(summary = "Used for Reset Password",
            description = "This API is used for Reset password.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfully Reset Password.",
                            content = @Content(schema = @Schema(implementation = User.class))),
                    @ApiResponse(responseCode = "400", description = "something went wrong"),
            })
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

    @Operation(summary = "Used for Logout",
            description = "This API is used for Logout.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Logout Successfully.",
                            content = @Content(schema = @Schema(implementation = User.class))),
                    @ApiResponse(responseCode = "400", description = "something went wrong"),
            })
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
