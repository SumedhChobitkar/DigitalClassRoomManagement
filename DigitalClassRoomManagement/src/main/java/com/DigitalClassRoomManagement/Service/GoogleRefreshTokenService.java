package com.DigitalClassRoomManagement.Service;

import com.DigitalClassRoomManagement.Entity.Teacher;
import com.DigitalClassRoomManagement.Entity.GoogleRefreshToken;

public interface GoogleRefreshTokenService {
    void saveRefreshToken(Teacher teacher, String refreshToken);
    GoogleRefreshToken findByTeacher(Teacher teacher);
}
