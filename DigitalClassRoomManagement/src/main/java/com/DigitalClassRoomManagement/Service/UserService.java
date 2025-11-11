package com.DigitalClassRoomManagement.Service;

import com.DigitalClassRoomManagement.Dto.userDto;
import com.DigitalClassRoomManagement.Entity.User;

import java.util.List;

public interface UserService {

    userDto registeration(User user1);
    User login(String email, String password);
    List<userDto> getAll();
    userDto getUserById(Long id);
}
