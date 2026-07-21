package com.example.clinicmvcspring.aspects;

import java.sql.Timestamp;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.example.clinicmvcspring.annotations.Audit;
import com.example.clinicmvcspring.models.AuditAction;
import com.example.clinicmvcspring.models.AuditLog;
import com.example.clinicmvcspring.services.AuditLogService;

import lombok.extern.slf4j.Slf4j;

@Aspect
@Component
@Slf4j
public class AuditingAspect {

    private final AuditLogService auditLogService;

    public AuditingAspect(AuditLogService auditLogService) {
        this.auditLogService = auditLogService;
    }

    private String getCurrentUsername() {
        var authentication = SecurityContextHolder.getContext()
                .getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()
                || "anonymousUser".equals(authentication.getPrincipal())) {
            return "SYSTEM";
        }

        return authentication.getName();
    }

    @Around("@annotation(auditAnnotation)") // annotation in the method parameters not in path like Log
    public Object auditActions(ProceedingJoinPoint joinPoint, Audit auditAnnotation) throws Throwable {
        String methodName = joinPoint.getSignature().getName();
        AuditLog auditLog;
        AuditAction action;

        try {
            Object result = joinPoint.proceed();
            auditLog = new AuditLog();
            action = auditAnnotation.action();
            auditLog.setActionType(action);
            auditLog.setMadeBy(getCurrentUsername()); // username
            auditLog.setPerformedAt(new Timestamp(System.currentTimeMillis()));
            auditLog.setDetails(methodName + " Done Successfully");

            auditLogService.addAuditLog(auditLog);
            return result;

        } catch (Exception e) {
            auditLog = new AuditLog();
            action = auditAnnotation.action();
            auditLog.setActionType(action);
            auditLog.setMadeBy(getCurrentUsername()); // username
            auditLog.setPerformedAt(new Timestamp(System.currentTimeMillis()));
            auditLog.setDetails(methodName + "Failed, ERROR: " + e.getMessage());

            auditLogService.addAuditLog(auditLog);
            throw e;
        }

    }

}
