package com.DigitalClassRoomManagement.Dto;

public class AdminResponseDTO {

    private Long adminId;
    private String username;

    public AdminResponseDTO(Long adminId, String username) {
        this.adminId = adminId;
        this.username = username;
    }

    public Long getAdminId() {
        return adminId;
    }

    public String getUsername() {
        return username;
    }
}
