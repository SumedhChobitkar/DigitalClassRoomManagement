package com.DigitalClassRoomManagement.ServiceImpl;

import com.DigitalClassRoomManagement.Entity.AuditLog;
import com.DigitalClassRoomManagement.Repository.AuditLogRepository;
import com.DigitalClassRoomManagement.Service.AuditLogService;
import com.DigitalClassRoomManagement.commonUtil.ValidationClass;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuditLogServiceImpl implements AuditLogService {

    private static final Logger logger = LoggerFactory.getLogger(AuditLogServiceImpl.class);
    private final AuditLogRepository auditLogRepository;

    public AuditLogServiceImpl(AuditLogRepository auditLogRepository) {
        this.auditLogRepository = auditLogRepository;
    }

    @Override
    public AuditLog createAuditLog(AuditLog auditLog) {
        try {
            logger.info("Creating AuditLog: {}", auditLog);

            // Enum-safe validation
            ValidationClass.validateAuditLog(
                    auditLog.getUserId(),
                    auditLog.getUsername(),
                    auditLog.getAction() != null ? auditLog.getAction().name() : null,
                    auditLog.getModule() != null ? auditLog.getModule().name() : null,
                    auditLog.getTime()
            );

            AuditLog saved = auditLogRepository.save(auditLog);
            logger.info("AuditLog created with ID: {}", saved.getAuditLogId());
            return saved;

        } catch (IllegalArgumentException e) {
            logger.warn("Validation error while creating AuditLog: {}", e.getMessage());
            throw e;

        } catch (Exception e) {
            logger.error("Unexpected error while creating AuditLog: {}", e.getMessage(), e);
            throw new RuntimeException("Error creating AuditLog: " + e.getMessage(), e);
        }
    }

    @Override
    public AuditLog getAuditLogById(Long id) {
        try {
            logger.info("Fetching AuditLog ID: {}", id);
            return auditLogRepository.findById(id)
                    .orElseThrow(() -> {
                        String msg = "AuditLog not found with ID: " + id;
                        logger.warn(msg);
                        return new IllegalArgumentException(msg);
                    });

        } catch (IllegalArgumentException e) {
            throw e;

        } catch (Exception e) {
            logger.error("Unexpected error while fetching AuditLog ID {}: {}", id, e.getMessage(), e);
            throw new RuntimeException("Error fetching AuditLog: " + e.getMessage(), e);
        }
    }

    @Override
    public List<AuditLog> getAllAuditLogs() {
        try {
            logger.info("Fetching all AuditLogs");
            return auditLogRepository.findAll();

        } catch (Exception e) {
            logger.error("Unexpected error while fetching all AuditLogs: {}", e.getMessage(), e);
            throw new RuntimeException("Error fetching AuditLogs: " + e.getMessage(), e);
        }
    }

    @Override
    public AuditLog updateAuditLog(Long id, AuditLog auditLog) {
        try {
            logger.info("Updating AuditLog ID: {}", id);
            AuditLog existing = getAuditLogById(id);

            // Enum-safe validation
            ValidationClass.validateAuditLog(
                    auditLog.getUserId(),
                    auditLog.getUsername(),
                    auditLog.getAction() != null ? auditLog.getAction().name() : null,
                    auditLog.getModule() != null ? auditLog.getModule().name() : null,
                    auditLog.getTime()
            );

            existing.setUserId(auditLog.getUserId());
            existing.setUsername(auditLog.getUsername());
            existing.setAction(auditLog.getAction());
            existing.setModule(auditLog.getModule());
            existing.setTime(auditLog.getTime());

            AuditLog updated = auditLogRepository.save(existing);
            logger.info("AuditLog updated ID: {}", updated.getAuditLogId());
            return updated;

        } catch (IllegalArgumentException e) {
            throw e;

        } catch (Exception e) {
            logger.error("Unexpected error while updating AuditLog ID {}: {}", id, e.getMessage(), e);
            throw new RuntimeException("Error updating AuditLog: " + e.getMessage(), e);
        }
    }

    @Override
    public void deleteAuditLog(Long id) {
        try {
            logger.info("Deleting AuditLog ID: {}", id);
            AuditLog existing = getAuditLogById(id);
            auditLogRepository.delete(existing);
            logger.info("AuditLog deleted ID: {}", id);

        } catch (IllegalArgumentException e) {
            throw e;

        } catch (Exception e) {
            logger.error("Unexpected error while deleting AuditLog ID {}: {}", id, e.getMessage(), e);
            throw new RuntimeException("Error deleting AuditLog: " + e.getMessage(), e);
        }
    }
}
