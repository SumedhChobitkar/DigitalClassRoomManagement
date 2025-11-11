package com.DigitalClassRoomManagement.Service;

import com.DigitalClassRoomManagement.Dto.AdminResponseDTO;
import com.DigitalClassRoomManagement.Dto.UnApproveAdminsDto;
import com.DigitalClassRoomManagement.Entity.Admin;
import com.DigitalClassRoomManagement.Dto.AdminDTO;
import com.DigitalClassRoomManagement.Entity.UnAppproveAdmins;

import java.util.List;

public interface AdminService {

    public String makeAdmin(Long id);
    public List<AdminResponseDTO> getAllUnApproveAdmins();
    public UnAppproveAdmins unApproveAdminsRegister(UnApproveAdminsDto tempAdmin);
    public List<Admin> sendData();
    public String deleteById(Long id);
    public String updateById(Long id, AdminDTO username);
    public String GetData(Admin admin1);
}
