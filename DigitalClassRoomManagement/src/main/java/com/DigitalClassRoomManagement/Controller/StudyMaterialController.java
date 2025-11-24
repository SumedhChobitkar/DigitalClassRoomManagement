package com.DigitalClassRoomManagement.Controller;

import com.DigitalClassRoomManagement.Entity.StudyMaterial;
import com.DigitalClassRoomManagement.Service.StudyMaterialService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.sql.rowset.serial.SerialBlob;
import java.sql.Blob;
import java.time.LocalDate;
import java.util.List;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/study-materials")
@Tag(name = "Study Material Management", description = "APIs for managing study materials")
public class StudyMaterialController {

    @Autowired
    private StudyMaterialService studyMaterialService;

    @Operation(summary = "Upload new study material", description = "Uploads a new study material with file, title, and type.")
    @PostMapping(consumes = "multipart/form-data")
    public ResponseEntity<?> createMaterial(
            @RequestParam("title") String title,
            @RequestParam("type") String type,
            @RequestParam("file") MultipartFile file) {

        try {
            Blob blob = new SerialBlob(file.getBytes());
            StudyMaterial studyMaterial = new StudyMaterial();
            studyMaterial.setTitle(title);
            studyMaterial.setType(type);
            studyMaterial.setFileUrl(blob);
            studyMaterial.setUploadDate(LocalDate.now());

            StudyMaterial saved = studyMaterialService.saveMaterial(studyMaterial);
            return ResponseEntity.ok(saved);

        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error while creating material: " + e.getMessage());
        }
    }

    @Operation(summary = "Get all study materials", description = "Fetches all study materials from the database.")
    @GetMapping
    public ResponseEntity<?> getAllMaterials() {
        try {
            List<StudyMaterial> list = studyMaterialService.getAllMaterials();
            return ResponseEntity.ok(list);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error fetching materials: " + e.getMessage());
        }
    }

    @Operation(summary = "Get material by ID", description = "Fetches a single study material by its ID.")
    @GetMapping("/{id}")
    public ResponseEntity<?> getMaterialById(@PathVariable Long id) {
        try {
            return studyMaterialService.getMaterialById(id)
                    .map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error fetching material: " + e.getMessage());
        }
    }

    @Operation(summary = "Update study material", description = "Updates an existing study material. File is optional.")
    @PutMapping(value = "/{id}", consumes = "multipart/form-data")
    public ResponseEntity<?> updateMaterial(
            @PathVariable Long id,
            @RequestParam("title") String title,
            @RequestParam("type") String type,
            @RequestParam(value = "file", required = false) MultipartFile file) {

        try {
            StudyMaterial existing = studyMaterialService.getMaterialById(id)
                    .orElseThrow(() -> new RuntimeException("Material not found"));

            existing.setTitle(title);
            existing.setType(type);

            if (file != null && !file.isEmpty()) {
                Blob blob = new SerialBlob(file.getBytes());
                existing.setFileUrl(blob);
            }

            existing.setUploadDate(LocalDate.now());

            StudyMaterial updated = studyMaterialService.saveMaterial(existing);
            return ResponseEntity.ok(updated);

        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error updating material: " + e.getMessage());
        }
    }

    @Operation(summary = "Delete study material", description = "Deletes a study material by its ID.")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteMaterial(@PathVariable Long id) {
        try {
            boolean deleted = studyMaterialService.deleteMaterial(id);
            if (deleted)
                return ResponseEntity.ok("Material deleted successfully");
            else
                return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error deleting material: " + e.getMessage());
        }
    }
}
