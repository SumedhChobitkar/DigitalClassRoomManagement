package com.DigitalClassRoomManagement.Service;

import com.DigitalClassRoomManagement.Entity.user;
import com.DigitalClassRoomManagement.dto.userDto;

import java.util.List;

public interface UserService {

    userDto registeration(user user1);
    user login(String email,String password);
    List<userDto> getAll();
    userDto getUserById(Long id);
}
