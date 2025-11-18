package com.DigitalClassRoomManagement.Service;


import com.DigitalClassRoomManagement.Entity.Admin;

import com.DigitalClassRoomManagement.Dto.AdminDTO;
import org.springframework.http.ResponseEntity;

import jakarta.validation.Valid;


import java.util.List;

public interface AdminService {

    public List<Admin> sendData();
    public String deleteById(Long id);
    public String updateById(Long id, @Valid AdminDTO username);
    public String GetData(Admin admin1);
}
