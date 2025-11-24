package com.DigitalClassRoomManagement.Service;

import com.DigitalClassRoomManagement.Entity.AuditLog;

import java.util.List;

public interface AuditLogService {
    AuditLog createAuditLog(AuditLog auditLog);
    AuditLog getAuditLogById(Long id);
    List<AuditLog> getAllAuditLogs();
    AuditLog updateAuditLog(Long id, AuditLog auditLog);
    void deleteAuditLog(Long id);
}
