package com.example.clinicmvcspring.listeners;

import java.sql.Timestamp;

import org.springframework.context.event.EventListener;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.example.clinicmvcspring.events.UserLoggedInEvent;
import com.example.clinicmvcspring.models.AuditAction;
import com.example.clinicmvcspring.models.AuditLog;
import com.example.clinicmvcspring.services.AuditLogService;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Getter
@AllArgsConstructor
@Slf4j
@Component
public class UserEventListener {

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
    public void handleUserLogInAudit(UserLoggedInEvent event) {

        AuditLog auditLog = AuditLog.builder()
                .actionType(AuditAction.LOGIN)
                .madeBy(getCurrentUsername())
                .performedAt(new Timestamp(System.currentTimeMillis()))
                .details("User logged In: " + event.getLoggedInUsername())
                .build();

        auditService.addAuditLog(auditLog);
    }

    @EventListener
    public void handleUserLogInAppLog(UserLoggedInEvent event) {
        log.info("APP LOG: User " + event.getLoggedInUsername() + event.getMessage());
    }
}
