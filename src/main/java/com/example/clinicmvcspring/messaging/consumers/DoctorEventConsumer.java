package com.example.clinicmvcspring.messaging.consumers;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import com.example.clinicmvcspring.config.RabbitMQConstants;
import com.example.clinicmvcspring.messaging.DoctorCreatedMessage;
import com.example.clinicmvcspring.messaging.DoctorDeletedMessage;
import com.example.clinicmvcspring.messaging.DoctorUpdatedMessage;
import com.example.clinicmvcspring.models.AuditAction;
import com.example.clinicmvcspring.models.AuditLog;
import com.example.clinicmvcspring.services.AuditLogService;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class DoctorEventConsumer {

    private final AuditLogService auditLogService;

    public DoctorEventConsumer(AuditLogService auditLogService) {
        this.auditLogService = auditLogService;
    }

    // Listen to the created queue
    @RabbitListener(queues = RabbitMQConstants.QUEUE_DOCTOR_CREATED)
    public void handleDoctorCreated(DoctorCreatedMessage message) {
        log.info("APP LOG: A new doctor named {} joined the clinic (email: {})",
                message.doctorName(), message.doctorEmail());

        AuditLog auditLog = AuditLog.builder()
                .actionType(AuditAction.CREATE)
                .madeBy(message.username())
                .performedAt(message.happenedAt())
                .details("Doctor Created via RabbitMQ: " + message.doctorName())
                .build();

        auditLogService.addAuditLog(auditLog);
    }

    // Listen to the updated queue
    @RabbitListener(queues = RabbitMQConstants.QUEUE_DOCTOR_UPDATED)
    public void handleDoctorUpdated(DoctorUpdatedMessage message) {
        AuditLog auditLog = AuditLog.builder()
                .actionType(AuditAction.UPDATE)
                .madeBy(message.username())
                .performedAt(message.happenedAt())
                .details("Doctor Updated: " + message.doctorName())
                .build();

        auditLogService.addAuditLog(auditLog);
        log.info("Dr." + message.doctorName() + message.message());

    }

    // Listen to the deleted queue!
    @RabbitListener(queues = RabbitMQConstants.QUEUE_DOCTOR_DELETED)
    public void handleDoctorDeleted(DoctorDeletedMessage message) {
        AuditLog auditLog = AuditLog.builder()
                .actionType(AuditAction.DELETE)
                .madeBy(message.username())
                .performedAt(message.happenedAt())
                .details("Doctor Deleted: " + message.doctorName())
                .build();

        auditLogService.addAuditLog(auditLog);
        log.info("Dr." + message.doctorName() + message.message());
    }
}
