package com.DigitalClassRoomManagement.Service;

import com.DigitalClassRoomManagement.Dto.UserDto;
import com.DigitalClassRoomManagement.Entity.User;

import java.util.List;

public interface UserService {

    UserDto registeration(User user1);
    User login(String email, String password);
    List<UserDto> getAll();
    UserDto getUserById(Long id);
    String forgotPassword(String email);
    boolean verifyOtp(String email, String otp);
    String resetPassword(String email, String newPassword, String confirmPassword);
    String logout(Long userId);

}
