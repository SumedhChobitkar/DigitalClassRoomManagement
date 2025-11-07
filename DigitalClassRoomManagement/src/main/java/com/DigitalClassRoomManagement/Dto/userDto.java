package com.DigitalClassRoomManagement.Dto;

import com.DigitalClassRoomManagement.Entity.Role;
import lombok.Data;

@Data
public class userDto {
    private Long userId;
    private String name;
    private String email;
    private Role role;
    private String languagePreference;
}

