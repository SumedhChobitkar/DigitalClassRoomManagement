package com.DigitalClassRoomManagement.Service;

import com.DigitalClassRoomManagement.Dto.AdmissionDTO;

import java.util.List;

public interface AdmissionService {

    AdmissionDTO createAdmission(AdmissionDTO dto);

    List<AdmissionDTO> getAllAdmissions();

    AdmissionDTO getAdmissionById(Long id);

    AdmissionDTO updateAdmission(Long id, AdmissionDTO dto);

    void deleteAdmission(Long id);
}
