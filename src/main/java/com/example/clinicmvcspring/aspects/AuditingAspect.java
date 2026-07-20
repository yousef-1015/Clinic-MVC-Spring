package com.example.clinicmvcspring.aspects;

import java.sql.Timestamp;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

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

    @Around("execution(* com.example.clinicmvcspring.services.DoctorService.*(..))")
    public Object auditDoctorActions(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();

        try {
            return joinPoint.proceed();

        } finally {
            long timeTaken = System.currentTimeMillis() - startTime;

            AuditLog auditLog = new AuditLog();
            auditLog.setActionType(joinPoint.getSignature().getName());// method name is the action that was made
            auditLog.setMadeBy(getCurrentUsername()); // username
            auditLog.setPerformedAt(new Timestamp(System.currentTimeMillis()));
            auditLog.setDetails("Action executed in " + timeTaken + " ms");

            auditLogService.addAuditLog(auditLog);
        }
    }

}
