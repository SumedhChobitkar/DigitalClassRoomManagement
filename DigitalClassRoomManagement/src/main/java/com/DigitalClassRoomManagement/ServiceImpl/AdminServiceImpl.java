package com.DigitalClassRoomManagement.ServiceImpl;


import com.DigitalClassRoomManagement.Entity.Admin;
import com.DigitalClassRoomManagement.Enum.Role;
import com.DigitalClassRoomManagement.Exception.ResourceNotFoundException;
import com.DigitalClassRoomManagement.Repository.AdminRepository;
import com.DigitalClassRoomManagement.Service.AdminService;

import com.DigitalClassRoomManagement.Dto.AdminDTO;

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
        Admin admin = adminRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cannot find the Admin with ID: " + id));

        if (adminDTO.getFirstName() != null && !adminDTO.getFirstName().isBlank()) {
            admin.setFirstName(adminDTO.getFirstName());
        }


        if (adminDTO.getLastName() != null && !adminDTO.getLastName().isBlank()) {
            admin.setLastName(adminDTO.getLastName());
        }


        if (adminDTO.getEmail() != null && !adminDTO.getEmail().isBlank()) {
            admin.setEmail(adminDTO.getEmail());
        }


        if (adminDTO.getPhone() != null && !adminDTO.getPhone().isBlank()) {
            admin.setPhone(adminDTO.getPhone());
        }


        if (adminDTO.getQualification() != null && !adminDTO.getQualification().isBlank()) {
            admin.setQualification(adminDTO.getQualification());
        }


        if (adminDTO.getExperienceYears() != null) {
            admin.setExperienceYears(adminDTO.getExperienceYears());
        }


        if (adminDTO.getGender() != null && !adminDTO.getGender().isBlank()) {
            admin.setGender(adminDTO.getGender());
        }


        if (adminDTO.getDateOfBirth() != null && !adminDTO.getDateOfBirth().isBlank()) {
            admin.setDateOfBirth(adminDTO.getDateOfBirth());
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
        admin.setRole(Role.ADMIN);
        adminRepo.save(admin);
        return "Data saved successfully";
    }
}
