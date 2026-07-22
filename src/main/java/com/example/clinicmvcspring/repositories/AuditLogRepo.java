package com.example.clinicmvcspring.repositories;

import java.sql.Timestamp;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

import com.example.clinicmvcspring.models.AuditLog;

public interface AuditLogRepo extends JpaRepository<AuditLog, Integer> {
    @Transactional
    @Modifying
    int deleteByPerformedAtBefore(Timestamp cutoffDate);// DELETE FROM audit_logs WHERE performed_at < ?; from JPA\
    
}
