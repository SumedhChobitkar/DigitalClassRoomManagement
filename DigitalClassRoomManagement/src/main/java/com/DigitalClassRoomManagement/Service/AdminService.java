package com.DigitalClassRoomManagement.Service;

import com.DigitalClassRoomManagement.Dto.AdminDTO;
import com.DigitalClassRoomManagement.Entity.Admin;
import jakarta.validation.Valid;

import java.util.List;

public interface AdminService {

    public List<Admin> sendData();
    public String deleteById(Long id);
    public String updateById(Long id, @Valid AdminDTO username);
    public String GetData(Admin admin1);
}
