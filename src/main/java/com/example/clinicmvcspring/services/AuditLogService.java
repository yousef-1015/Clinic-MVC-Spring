package com.example.clinicmvcspring.services;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.example.clinicmvcspring.models.AuditLog;
import com.example.clinicmvcspring.repositories.AuditLogRepo;

@Service
public class AuditLogService {

    private final AuditLogRepo repo;

    public AuditLogService(AuditLogRepo repo) {
        this.repo = repo;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW) // for aufiting failed in Transaction
    public AuditLog addAuditLog(AuditLog audit) {
        return repo.save(audit);
    }

    public List<AuditLog> getAllAuditLogs() {
        return repo.findAll();
    }

    public Optional<AuditLog> getAuditLogByID(int id) {
        return repo.findById(id);
    }

    public void deleteAuditLog(AuditLog audit) {
        repo.delete(audit);
    }

    public void deleteAuditLogByID(int id) {
        repo.deleteById(id);
    }

    public AuditLog updateAuditLogById(int id, AuditLog audit) {
        audit.setId(id);
        return repo.save(audit);
    }

    public Page<AuditLog> getAllAuditLogs(Pageable pageable) {
        return repo.findAll(pageable);
    }

    @Scheduled(cron = "0 0 3 * * *")
    @Transactional
    public void cleanUpOldAuditLogs() {
        Timestamp twoWeeksAgo = Timestamp.valueOf(LocalDateTime.now().minusWeeks(2));
        int deletedCount = repo.deleteByPerformedAtBefore(twoWeeksAgo);
        System.out.println("Cleanup: Deleted " + deletedCount + " audit logs older than two weeks.");
    }
}
