package com.DigitalClassRoomManagement.Dto;

import com.DigitalClassRoomManagement.Enum.Role;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Data;

@Data
public class UserDto {
    private Long userId;
    private String name;
    private String email;
    @Enumerated(EnumType.STRING)
    private Role role;
    private String languagePreference;
}

