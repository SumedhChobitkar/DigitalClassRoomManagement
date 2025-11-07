package com.DigitalClassRoomManagement.Controller;
import com.DigitalClassRoomManagement.Dto.MultimediaContentResponseDto;
import com.DigitalClassRoomManagement.Dto.MultimediaMetaUpdateDto;
import com.DigitalClassRoomManagement.Service.MultimediaContentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

@RestController
@RequestMapping("/api/v1/multimedia")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@Slf4j
public class MultimediaContentController {

    private final MultimediaContentService service;

    // Which Type Of File You upload Select:-
    //  Upload Multimedia File (Audio / Video / Animation / Image / PDF)
    //@PostMapping("/api/v1/multimedia/Audio / Video / Animation / Image / PDF")
    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<MultimediaContentResponseDto> uploadFile(
            @RequestPart("title") String title,
            @RequestPart("type") String type,
            @RequestPart("file") MultipartFile file) {
        log.info("Received request to upload file: title='{}', type='{}'", title, type);
        try {
            MultimediaContentResponseDto response = service.uploadFile(title, type, file);
            log.info("File uploaded successfully: {}", response.getFileName());
            return ResponseEntity.ok(response);
        } catch (Exception ex) {
            log.error("Error while uploading file: title='{}', type='{}'", title, type, ex);
            throw ex;
        }
    }

    // Add YouTube Link
    //@PostMapping("/api/v1/multimedia/youtube")
    @PostMapping("/youtube")
    public ResponseEntity<MultimediaContentResponseDto> addYoutubeLink(
            @RequestParam String title,
            @RequestParam String url) {
        log.info("Adding YouTube link for title='{}'", title);
        try {
            MultimediaContentResponseDto response = service.addYoutube(title, url);
            log.info("YouTube link added successfully for title='{}'", title);
            return ResponseEntity.ok(response);
        } catch (Exception ex) {
            log.error("Error while adding YouTube link for title='{}'", title, ex);
            throw ex;
        }
    }

    // Download Multimedia File
    //@GetMapping("/api/v1/multimedia/{id}/download")
    @GetMapping("/{id}/download")
    public ResponseEntity<Resource> downloadFile(@PathVariable Long id) {
        log.info("Download request received for file ID={}", id);
        try {
            MultimediaContentResponseDto meta = service.getOne(id);
            Resource data = service.downloadFile(id);
            String fileName = meta.getFileName() != null
                    ? meta.getFileName()
                    : meta.getTitle() + "_file";
            String encodedFileName = URLEncoder.encode(fileName, StandardCharsets.UTF_8);
            log.info("File downloaded successfully for ID={}", id);
            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(
                            meta.getContentType() == null ? MediaType.APPLICATION_OCTET_STREAM_VALUE : meta.getContentType()))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename*=UTF-8''" + encodedFileName)
                    .body(data);
        } catch (Exception ex) {
            log.error("Error while downloading file with ID={}", id, ex);
            throw ex;
        }
    }

    //Get Multimedia Metadata by ID
    //@GetMapping("/api/v1/multimedia/{id}")
    @GetMapping("/{id}")
    public ResponseEntity<MultimediaContentResponseDto> getMultimediaById(@PathVariable Long id) {
        log.info("Fetching multimedia details for ID={}", id);
        try {
            MultimediaContentResponseDto response = service.getOne(id);
            log.info("Successfully fetched multimedia details for ID={}", id);
            return ResponseEntity.ok(response);
        } catch (Exception ex) {
            log.error("Error fetching multimedia details for ID={}", id, ex);
            throw ex;
        }
    }

    //Get All Multimedia (or Filter by Type)
    //@GetMapping("/api/v1/multimedia?type=VIDEO")
    @GetMapping
    public ResponseEntity<List<MultimediaContentResponseDto>> getAllMultimedia(
            @RequestParam(required = false) String type) {
        log.info("Fetching all multimedia files, filter by type='{}'", type);
        try {
            List<MultimediaContentResponseDto> response = service.listAll(type);
            log.info("Fetched {} multimedia entries", response.size());
            return ResponseEntity.ok(response);
        } catch (Exception ex) {
            log.error("Error fetching multimedia list", ex);
            throw ex;
        }
    }

    //  Update Multimedia Metadata (title/type)
    //@PutMapping("/api/v1/multimedia/{id}")
    @PutMapping("/{id}")
    public ResponseEntity<MultimediaContentResponseDto> updateMultimediaMeta(
            @PathVariable Long id,
            @Valid @RequestBody MultimediaMetaUpdateDto dto) {
        log.info("Updating multimedia metadata for ID={}, new title='{}'", id, dto.getTitle());
        try {
            MultimediaContentResponseDto response = service.updateMeta(id, dto);
            log.info("Successfully updated metadata for ID={}", id);
            return ResponseEntity.ok(response);
        } catch (Exception ex) {
            log.error("Error updating multimedia metadata for ID={}", id, ex);
            throw ex;
        }
    }

    //Delete Multimedia
    //@DeleteMapping("/api/v1/multimedia/{id}")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMultimedia(@PathVariable Long id) {
        log.info("Deleting multimedia with ID={}", id);
        try {
            service.delete(id);
            log.info("Successfully deleted multimedia ID={}", id);
            return ResponseEntity.noContent().build();
        } catch (Exception ex) {
            log.error("Error deleting multimedia with ID={}", id, ex);
            throw ex;
        }
    }
}
