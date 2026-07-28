package com.example.clinicmvcspring.listeners;

import java.sql.Timestamp;

import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.example.clinicmvcspring.events.DoctorCreatedEvent;
import com.example.clinicmvcspring.events.DoctorDeletedEvent;
import com.example.clinicmvcspring.events.DoctorUpdatedEvent;
import com.example.clinicmvcspring.models.AuditAction;
import com.example.clinicmvcspring.models.AuditLog;
import com.example.clinicmvcspring.services.AuditLogService;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@AllArgsConstructor
@Slf4j
public class DoctorEventListener {

    private final AuditLogService auditService;

    private String getCurrentUsername() {
        var authentication = SecurityContextHolder.getContext()
                .getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()
                || "anonymousUser".equals(authentication.getPrincipal())) {
            return "SYSTEM";
        }

        return authentication.getName();
    }

    @EventListener
    @Async
    public void handleDoctorCreatedAudit(DoctorCreatedEvent event) {

        AuditLog auditLog = AuditLog.builder()
                .actionType(AuditAction.CREATE)
                .madeBy(getCurrentUsername())
                .performedAt(new Timestamp(System.currentTimeMillis()))
                .details("Doctor Created: " + event.getDoctorName())
                .build();

        auditService.addAuditLog(auditLog);
    }

    @EventListener
    @Async
    public void handleDoctorCreatedAppLog(DoctorCreatedEvent event) {
        log.info("APP LOG: A new doctor named " + event.getDoctorName() + " joined the clinic");
    }

    @EventListener
    @Async
    public void handleDoctorUpdatedAudit(DoctorUpdatedEvent event) {

        AuditLog auditLog = AuditLog.builder()
                .actionType(AuditAction.UPDATE)
                .madeBy(getCurrentUsername())
                .performedAt(new Timestamp(System.currentTimeMillis()))
                .details("Doctor Updated: " + event.getDoctorName())
                .build();

        auditService.addAuditLog(auditLog);
    }

    @EventListener
    @Async
    public void handleDoctorAppLog(DoctorUpdatedEvent event) {
        log.info("Dr." + event.getDoctorName() + " " + event.getMessage());
    }

    @EventListener
    @Async
    public void handleDoctorDeletedAudit(DoctorDeletedEvent event) {

        AuditLog auditLog = AuditLog.builder()
                .actionType(AuditAction.DELETE)
                .madeBy(getCurrentUsername())
                .performedAt(new Timestamp(System.currentTimeMillis()))
                .details("Doctor Deleted: " + event.getDoctorName())
                .build();

        auditService.addAuditLog(auditLog);
    }

    @EventListener
    @Async
    public void handleDoctorDeletedAppLog(DoctorDeletedEvent event) {
        log.info("Dr." + event.getDoctorName() + " " + event.getMessage());
    }

}
