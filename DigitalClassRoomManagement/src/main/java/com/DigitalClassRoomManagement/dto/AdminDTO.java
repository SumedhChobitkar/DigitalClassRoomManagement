package com.DigitalClassRoomManagement.Dto;


import com.DigitalClassRoomManagement.Enum.AdminRole;
import com.DigitalClassRoomManagement.Enum.Status;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

public class AdminDTO {
    private Long adminId;
    private String password;
    @Enumerated(EnumType.STRING)
    private AdminRole role;
    @Enumerated(EnumType.STRING)
    private Status status;
    private String username;

    public Long getAdminId() {
        return adminId;
    }

    public void setAdminId(Long adminId) {
        this.adminId = adminId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public AdminRole getRole() {
        return role;
    }

    public void setRole(AdminRole role) {
        this.role = role;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
