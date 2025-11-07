package com.DigitalClassRoomManagement.ServiceImpl;

import com.DigitalClassRoomManagement.Dto.MultimediaContentResponseDto;
import com.DigitalClassRoomManagement.Dto.MultimediaMetaUpdateDto;
import com.DigitalClassRoomManagement.Entity.MultimediaContent;
import com.DigitalClassRoomManagement.Exception.BadRequestException;
import com.DigitalClassRoomManagement.Exception.ResourceNotFoundException;
import com.DigitalClassRoomManagement.Repository.MultimediaContentRepository;
import com.DigitalClassRoomManagement.Service.MultimediaContentService;
import com.DigitalClassRoomManagement.commonUtil.MultimediaValidationClass;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class MultimediaContentServiceImpl implements MultimediaContentService {

    @Autowired
    private MultimediaContentRepository repo;

    private static final Logger logger = LoggerFactory.getLogger(MultimediaContentServiceImpl.class);
    private static final DateTimeFormatter ISO = DateTimeFormatter.ISO_INSTANT;

    // -------------------- Upload FILE (AUDIO/VIDEO/IMAGE/ANIMATION/PDF) --------------------
    @Override
    public MultimediaContentResponseDto uploadFile(String title, String type, MultipartFile file) {
        try {
            logger.info("Uploading file: title={}, type={}, size={}", title, type, file == null ? null : file.getSize());
            validateUpload(title, type, file);

            byte[] bytes;
            try {
                //FIX: handle IOException safely
                bytes = file.getBytes();
            } catch (java.io.IOException ioException) {
                logger.error("IOException while reading uploaded file: {}", ioException.getMessage(), ioException);
                throw new BadRequestException("Could not read uploaded file: " + ioException.getMessage());
            }

            MultimediaContent mc = MultimediaContent.builder()
                    .title(title)
                    .type(type.trim().toUpperCase())
                    .fileName(file.getOriginalFilename())
                    .contentType(file.getContentType())
                    .fileSize(file.getSize())
                    .data(bytes)
                    .build();

            mc = repo.save(mc);
            System.out.println("Saved MultimediaContent with id = " + mc.getContentId());
            return toDto(mc);

        } catch (Exception e) {
            logger.error("Error in uploadFile: {}", e.getMessage(), e);
            if (e instanceof BadRequestException) throw e;
            throw new BadRequestException("Could not store file: " + e.getMessage());
        }
    }

    // -------------------- Add YOUTUBE link --------------------
    @Override
    public MultimediaContentResponseDto addYoutube(String title, String url) {
        try {
            logger.info("Adding YouTube link: title={}, url={}", title, url);
            validateYoutube(title, url);

            MultimediaContent mc = MultimediaContent.builder()
                    .title(title)
                    .type("YOUTUBE")
                    .url(url.trim())
                    .build();

            mc = repo.save(mc);
            return toDto(mc);

        } catch (Exception e) {
            logger.error("Error in addYoutube: {}", e.getMessage(), e);
            if (e instanceof BadRequestException) throw e;
            throw new BadRequestException("Could not save YouTube link: " + e.getMessage());
        }
    }

    // -------------------- Download FILE --------------------
    @Override
    @Transactional(readOnly = true)
    public Resource downloadFile(Long id) {
        try {
            Optional<MultimediaContent> opt = repo.findById(id);
            if (opt.isEmpty()) {
                throw new ResourceNotFoundException("Content not found with id " + id);
            }
            MultimediaContent mc = opt.get();

            if (mc.getData() == null) {
                throw new BadRequestException("This content has no file to download (probably a YOUTUBE link).");
            }
            return new ByteArrayResource(mc.getData());

        } catch (Exception e) {
            logger.error("Error in downloadFile: {}", e.getMessage(), e);
            throw e;
        }
    }

    // -------------------- Get one (metadata) --------------------
    @Override
    @Transactional(readOnly = true)
    public MultimediaContentResponseDto getOne(Long id) {
        try {
            Optional<MultimediaContent> opt = repo.findById(id);
            if (opt.isEmpty()) {
                throw new ResourceNotFoundException("Content not found with id " + id);
            }
            return toDto(opt.get());
        } catch (Exception e) {
            logger.error("Error in getOne: {}", e.getMessage(), e);
            throw e;
        }
    }

    // -------------------- List all / filter by type --------------------
    @Override
    @Transactional(readOnly = true)
    public List<MultimediaContentResponseDto> listAll(String type) {
        try {
            List<MultimediaContent> list;
            if (type == null || type.isBlank()) {
                list = repo.findAll();
            } else {
                list = repo.findByTypeIgnoreCase(type);
            }
            return list.stream().map(this::toDto).collect(Collectors.toList());
        } catch (Exception e) {
            logger.error("Error in listAll: {}", e.getMessage(), e);
            throw e;
        }
    }

    // -------------------- Update metadata (title) --------------------
    @Override
    public MultimediaContentResponseDto updateMeta(Long id, MultimediaMetaUpdateDto dto) {
        try {
            Optional<MultimediaContent> opt = repo.findById(id);
            if (opt.isEmpty()) {
                throw new ResourceNotFoundException("Content not found with id " + id);
            }
            if (!MultimediaValidationClass.isValidTitle(dto.getTitle())) {
                throw new BadRequestException("Invalid title format.");
            }

            MultimediaContent mc = opt.get();
            mc.setTitle(dto.getTitle());
            // JPA dirty-checking will update on transaction commit
            return toDto(mc);

        } catch (Exception e) {
            logger.error("Error in updateMeta: {}", e.getMessage(), e);
            throw e;
        }
    }

    // -------------------- Delete --------------------
    @Override
    public void delete(Long id) {
        try {
            if (!repo.existsById(id)) {
                throw new ResourceNotFoundException("Content not found with id " + id);
            }
            repo.deleteById(id);
        } catch (Exception e) {
            logger.error("Error in delete: {}", e.getMessage(), e);
            throw e;
        }
    }

    // -------------------- Validation helpers --------------------
    private static void validateUpload(String title, String type, MultipartFile file) {
        if (title == null || !MultimediaValidationClass.isValidTitle(title)) {
            throw new BadRequestException("Invalid Title: Start with uppercase; letters/numbers/space/_/- allowed.");
        }
        if (type == null || !MultimediaValidationClass.isValidType(type)) {
            throw new BadRequestException("Invalid Type: Allowed VIDEO, AUDIO, ANIMATION, IMAGE, PDF, YOUTUBE.");
        }
        if (type.equalsIgnoreCase("YOUTUBE")) {
            throw new BadRequestException("For YOUTUBE, use /youtube endpoint with URL (no file).");
        }
        if (file == null || file.isEmpty()) {
            throw new BadRequestException("File is required.");
        }
        if (file.getOriginalFilename() != null &&
                !MultimediaValidationClass.isValidFileName(file.getOriginalFilename())) {
            throw new BadRequestException("Invalid file name.");
        }
        if (file.getContentType() == null ||
                !MultimediaValidationClass.isValidContentType(file.getContentType())) {
            throw new BadRequestException("Unsupported contentType. Use video/*, audio/*, image/*, or application/pdf.");
        }
        if (!MultimediaValidationClass.isFileSizeAllowed(file.getSize())) {
            throw new BadRequestException("File too large. Max 512MB.");
        }
    }

    private static void validateYoutube(String title, String url) {
        if (title == null || !MultimediaValidationClass.isValidTitle(title)) {
            throw new BadRequestException("Invalid Title.");
        }
        if (url == null || !MultimediaValidationClass.isValidYoutubeUrl(url)) {
            throw new BadRequestException("Invalid YouTube URL.");
        }
    }

    // -------------------- Mapper --------------------
    private MultimediaContentResponseDto toDto(MultimediaContent m) {
        return MultimediaContentResponseDto.builder()
                .contentId(m.getContentId())
                .title(m.getTitle())
                .type(m.getType())
                .url(m.getUrl())
                .contentType(m.getContentType())
                .fileSize(m.getFileSize())
                .fileName(m.getFileName())
                .createdAt(m.getCreatedAt() == null ? null : ISO.format(m.getCreatedAt()))
                .updatedAt(m.getUpdatedAt() == null ? null : ISO.format(m.getUpdatedAt()))
                .build();
    }
}
