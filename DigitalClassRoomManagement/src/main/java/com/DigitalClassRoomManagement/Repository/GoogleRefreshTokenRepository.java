package com.DigitalClassRoomManagement.Repository;

import com.DigitalClassRoomManagement.Entity.GoogleRefreshToken;
import com.DigitalClassRoomManagement.Entity.Teacher;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface GoogleRefreshTokenRepository extends JpaRepository<GoogleRefreshToken, Long> {
    Optional<GoogleRefreshToken> findByTeacher(Teacher teacher);
}
