package com.example.clinicmvcspring.messaging.consumers;

import java.sql.Timestamp;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import com.example.clinicmvcspring.messaging.UserLoggedInMessage;
import com.example.clinicmvcspring.models.AuditAction;
import com.example.clinicmvcspring.models.AuditLog;
import com.example.clinicmvcspring.services.AuditLogService;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class UserEvnetConsumer {
    private final AuditLogService auditLogService;

    public UserEvnetConsumer(AuditLogService auditLogService) {
        this.auditLogService = auditLogService;
    }

    // Listen to the user logged in queue
    @RabbitListener(queues = "user.loggedin.queue")
    public void handleDoctorCreated(UserLoggedInMessage message) {
        log.info("APP LOG: User (" + message.loggedInUsername() + ") " + message.message());

        AuditLog auditLog = AuditLog.builder()
                .actionType(AuditAction.LOGIN)
                .madeBy("SYSTEM_RABBITMQ") // Because this is background process now
                .performedAt(new Timestamp(System.currentTimeMillis()))
                .details("user logged in via RabbitMQ: " + message.loggedInUsername())
                .build();

        auditLogService.addAuditLog(auditLog);
    }
}
