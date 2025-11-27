package com.DigitalClassRoomManagement.Service;

import com.DigitalClassRoomManagement.Entity.User;
import com.DigitalClassRoomManagement.Enum.Status;

import java.util.List;

public interface SuperAdminService {
    public List<User> getunApprovedStatusRequest();
    public List<User> getApprovedStatusRequest();
    public User updateStatus(Long id, Status status);
}
