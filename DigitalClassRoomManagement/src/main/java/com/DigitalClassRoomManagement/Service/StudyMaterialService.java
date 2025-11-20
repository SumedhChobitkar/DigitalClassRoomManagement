package com.DigitalClassRoomManagement.Service;

import com.DigitalClassRoomManagement.Entity.StudyMaterial;

import java.util.List;
import java.util.Optional;

public interface StudyMaterialService {

    StudyMaterial saveMaterial(StudyMaterial studyMaterial);
    List<StudyMaterial> getAllMaterials();
    Optional<StudyMaterial> getMaterialById(Long id);

    StudyMaterial updateMaterial(Long id, StudyMaterial updatedMaterial);

    boolean deleteMaterial(Long id);
}


