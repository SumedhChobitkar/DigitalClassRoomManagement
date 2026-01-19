package com.DigitalClassRoomManagement.ServiceImpl;

import com.DigitalClassRoomManagement.Dto.AdmissionDTO;
import com.DigitalClassRoomManagement.Entity.Admission;
import com.DigitalClassRoomManagement.Exception.AdmissionNotFoundException;
import com.DigitalClassRoomManagement.Repository.AdmissionRepository;
import com.DigitalClassRoomManagement.Service.AdmissionService;
import com.DigitalClassRoomManagement.commonUtil.ValidationClass;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AdmissionServiceImpl implements AdmissionService {

    private static final Logger logger =
            LoggerFactory.getLogger(AdmissionServiceImpl.class);

    private final AdmissionRepository repository;

    public AdmissionServiceImpl(AdmissionRepository repository) {
        this.repository = repository;
    }

    @Override
    public AdmissionDTO createAdmission(AdmissionDTO dto) {
        logger.info("Creating admission for {}", dto.getStudentName());

        // 🔹 Admission Validation
        ValidationClass.validateAdmission(
                dto.getStudentName(),
                dto.getEmail(),
                dto.getCourse(),
                dto.getAdmissionDate()
        );

        Admission admission = mapToEntity(dto);
        Admission saved = repository.save(admission);

        logger.info("Admission created with ID {}", saved.getAdmissionId());
        return mapToDTO(saved);
    }

    @Override
    public List<AdmissionDTO> getAllAdmissions() {
        logger.info("Fetching all admissions");

        return repository.findAll()
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public AdmissionDTO getAdmissionById(Long id) {
        logger.info("Fetching admission with ID {}", id);

        Admission admission = repository.findById(id)
                .orElseThrow(() ->
                        new AdmissionNotFoundException("Admission not found with id " + id));

        return mapToDTO(admission);
    }

    @Override
    public AdmissionDTO updateAdmission(Long id, AdmissionDTO dto) {
        logger.info("Updating admission with ID {}", id);

        // 🔹 Admission Validation
        ValidationClass.validateAdmission(
                dto.getStudentName(),
                dto.getEmail(),
                dto.getCourse(),
                dto.getAdmissionDate()
        );

        Admission admission = repository.findById(id)
                .orElseThrow(() ->
                        new AdmissionNotFoundException("Admission not found with id " + id));

        admission.setStudentName(dto.getStudentName());
        admission.setEmail(dto.getEmail());
        admission.setCourse(dto.getCourse());
        admission.setAdmissionDate(dto.getAdmissionDate());

        return mapToDTO(repository.save(admission));
    }

    @Override
    public void deleteAdmission(Long id) {
        logger.warn("Deleting admission with ID {}", id);

        Admission admission = repository.findById(id)
                .orElseThrow(() ->
                        new AdmissionNotFoundException("Admission not found with id " + id));

        repository.delete(admission);
    }

    // ===== Mapper Methods =====
    private AdmissionDTO mapToDTO(Admission admission) {
        AdmissionDTO dto = new AdmissionDTO();
        dto.setAdmissionId(admission.getAdmissionId());
        dto.setStudentName(admission.getStudentName());
        dto.setEmail(admission.getEmail());
        dto.setCourse(admission.getCourse());
        dto.setAdmissionDate(admission.getAdmissionDate());
        return dto;
    }

    private Admission mapToEntity(AdmissionDTO dto) {
        Admission admission = new Admission();
        admission.setStudentName(dto.getStudentName());
        admission.setEmail(dto.getEmail());
        admission.setCourse(dto.getCourse());
        admission.setAdmissionDate(dto.getAdmissionDate());
        return admission;
    }
}
