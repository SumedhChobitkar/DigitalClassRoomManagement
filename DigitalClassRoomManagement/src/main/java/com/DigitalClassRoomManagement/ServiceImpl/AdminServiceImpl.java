package com.DigitalClassRoomManagement.ServiceImpl;

import com.DigitalClassRoomManagement.Entity.Admin;
import com.DigitalClassRoomManagement.Exception.ResourceNotFoundException;
import com.DigitalClassRoomManagement.Repository.AdminRepository;
import com.DigitalClassRoomManagement.Service.AdminService;
import com.DigitalClassRoomManagement.dto.AdminDTO;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AdminServiceImpl implements AdminService {

    private static final Logger logger = LoggerFactory.getLogger(AdminServiceImpl.class);

    @Autowired
    private AdminRepository adminRepo;

    @Override
    public List<Admin> sendData() {
        return adminRepo.findAll();
    }

    @Override
    public String deleteById(Long id) {
        Optional<Admin> adminOpt = adminRepo.findById(id);

        if (adminOpt.isEmpty()) {
            logger.error("Cannot find the admin with the Admin ID");
            throw new ResourceNotFoundException("Cannot find the given Admin with ID: " + id);
        }

        adminRepo.deleteById(id);
        return "Admin deleted successfully";
    }
    @Override
    public String updateById(Long id, @Valid AdminDTO adminDTO) {
        Optional<Admin> dummyAdmin = adminRepo.findById(id);
        if(!dummyAdmin.isPresent()){
            logger.error("Cannot find the user with "+id +" id");
            throw new ResourceNotFoundException("Cannot find the given Admin with ID: " + id);
        }
        Admin admin = dummyAdmin.get();
        if (adminDTO.getUsername() != null && !adminDTO.getUsername().isBlank()) {
            admin.setUsername(adminDTO.getUsername());
        }
        if (adminDTO.getPassword() != null && !adminDTO.getPassword().isBlank()) {
            admin.setPassword(adminDTO.getPassword());
        }
        if (adminDTO.getRole() != null) {
            admin.setRole(adminDTO.getRole());
        }
        if (adminDTO.getStatus() != null) {
            admin.setStatus(adminDTO.getStatus());
        }

        adminRepo.save(admin);
        return "Admin updated successfully";
    }

    @Override
    public String GetData(@Valid Admin admin) {
        adminRepo.save(admin);
        return "Data saved successfully";
    }
}
