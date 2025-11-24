package com.DigitalClassRoomManagement.ServiceImpl;

import com.DigitalClassRoomManagement.Entity.StudyMaterial;
import com.DigitalClassRoomManagement.Repository.StudyMaterialRepository;
import com.DigitalClassRoomManagement.Service.StudyMaterialService;
import com.DigitalClassRoomManagement.commonUtil.ValidationClass;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class StudyMaterialServiceImpl implements StudyMaterialService {

    private static final Logger logger = LoggerFactory.getLogger(StudyMaterialServiceImpl.class);

    @Autowired
    private StudyMaterialRepository studyMaterialRepository;

    @Override
    public StudyMaterial saveMaterial(StudyMaterial studyMaterial) {
        logger.info("Saving new StudyMaterial: {}", studyMaterial.getTitle());
        try {
            studyMaterial.setUploadDate(LocalDate.now());
            validateStudyMaterial(studyMaterial);

            StudyMaterial savedMaterial = studyMaterialRepository.save(studyMaterial);
            logger.info("Successfully saved StudyMaterial with ID: {}", savedMaterial.getMaterialId());
            return savedMaterial;

        } catch (IllegalArgumentException e) {
            logger.warn("Validation failed while saving StudyMaterial: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Error saving study material: {}", e.getMessage(), e);
            throw new RuntimeException("Error saving study material: " + e.getMessage());
        }
    }

    @Override
    public List<StudyMaterial> getAllMaterials() {
        logger.info("Fetching all study materials...");
        List<StudyMaterial> materials = studyMaterialRepository.findAll();
        logger.info("Total materials found: {}", materials.size());
        return materials;
    }

    @Override
    public Optional<StudyMaterial> getMaterialById(Long id) {
        logger.info("Fetching StudyMaterial by ID: {}", id);
        Optional<StudyMaterial> material = studyMaterialRepository.findById(id);
        if (material.isPresent()) {
            logger.info("Found StudyMaterial: {}", material.get().getTitle());
        } else {
            logger.warn("StudyMaterial not found with ID: {}", id);
        }
        return material;
    }

    @Override
    public StudyMaterial updateMaterial(Long id, StudyMaterial updatedMaterial) {
        logger.info("Updating StudyMaterial with ID: {}", id);
        return studyMaterialRepository.findById(id)
                .map(existing -> {
                    existing.setTitle(updatedMaterial.getTitle());
                    existing.setType(updatedMaterial.getType());
                    existing.setFileUrl(updatedMaterial.getFileUrl());
                    existing.setUploadDate(LocalDate.now());

                    validateStudyMaterial(existing);
                    StudyMaterial updated = studyMaterialRepository.save(existing);
                    logger.info("Successfully updated StudyMaterial with ID: {}", updated.getMaterialId());
                    return updated;
                })
                .orElseThrow(() -> {
                    logger.error("StudyMaterial not found with ID: {}", id);
                    return new RuntimeException("StudyMaterial not found with id " + id);
                });
    }

    @Override
    public boolean deleteMaterial(Long id) {
        logger.info("Deleting StudyMaterial with ID: {}", id);
        try {
            if (studyMaterialRepository.existsById(id)) {
                studyMaterialRepository.deleteById(id);
                logger.info("Successfully deleted StudyMaterial with ID: {}", id);
                return true;
            } else {
                logger.warn("StudyMaterial not found with ID: {}", id);
                return false;
            }
        } catch (Exception e) {
            logger.error("Error deleting StudyMaterial with ID {}: {}", id, e.getMessage(), e);
            throw new RuntimeException("Error deleting material: " + e.getMessage());
        }
    }

    private void validateStudyMaterial(StudyMaterial studyMaterial) {
        logger.debug("Validating StudyMaterial: {}", studyMaterial.getTitle());

        // Validate Title
        if (studyMaterial.getTitle() == null ||
                !ValidationClass.TITLE_PATTERN.matcher(studyMaterial.getTitle()).matches()) {
            throw new IllegalArgumentException("Invalid title format. Must start with a capital letter.");
        }

        // Validate Type
        boolean isValidType = false;
        for (String validType : ValidationClass.VALID_TYPES) {
            if (validType.equalsIgnoreCase(studyMaterial.getType())) {
                isValidType = true;
                break;
            }
        }
        if (!isValidType) {
            throw new IllegalArgumentException("Invalid material type. Allowed types are: Notes, Worksheet, LessonPlan.");
        }

        // Validate Upload Date
        if (studyMaterial.getUploadDate() == null) {
            throw new IllegalArgumentException("Upload date cannot be null.");
        }
        if (studyMaterial.getUploadDate().isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("Upload date cannot be in the future.");
        }

        logger.debug("StudyMaterial validation successful for: {}", studyMaterial.getTitle());
    }
}
