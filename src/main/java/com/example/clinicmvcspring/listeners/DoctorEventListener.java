package com.example.clinicmvcspring.listeners;

import java.sql.Timestamp;

import org.springframework.context.event.EventListener;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.example.clinicmvcspring.events.DoctorCreatedEvent;
import com.example.clinicmvcspring.models.AuditAction;
import com.example.clinicmvcspring.models.AuditLog;
import com.example.clinicmvcspring.services.AuditLogService;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@AllArgsConstructor
@Slf4j
public class DoctorEventListener {

    private final AuditLogService auditServie;

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
    public void handleDoctorAudit(DoctorCreatedEvent event) {

        AuditLog auditLog = AuditLog.builder()
                .actionType(AuditAction.CREATE)
                .madeBy(getCurrentUsername())
                .performedAt(new Timestamp(System.currentTimeMillis()))
                .details("Doctor Created: " + event.getDoctorName())
                .build();

        auditServie.addAuditLog(auditLog);
    }

    @EventListener
    public void handleDoctorAppLog(DoctorCreatedEvent event) {
        log.info("APP LOG: A new doctor named " + event.getDoctorName() + " joined the clinic");
    }

}
