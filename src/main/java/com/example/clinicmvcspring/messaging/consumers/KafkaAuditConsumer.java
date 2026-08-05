package com.example.clinicmvcspring.messaging.consumers;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.example.clinicmvcspring.messaging.DoctorCreatedMessage;
import com.example.clinicmvcspring.messaging.DoctorDeletedMessage;
import com.example.clinicmvcspring.messaging.DoctorUpdatedMessage;
import com.example.clinicmvcspring.models.AuditAction;
import com.example.clinicmvcspring.models.AuditLog;
import com.example.clinicmvcspring.services.AuditLogService;
import java.sql.Timestamp;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class KafkaAuditConsumer {
    private final AuditLogService auditLogService;

    public KafkaAuditConsumer(AuditLogService auditLogService) {
        this.auditLogService = auditLogService;
    }

    @KafkaListener(topics = "doctor-events", groupId = "auditing-group")
    public void handleDoctorEvent(ConsumerRecord<String, Object> record) {
        AuditAction action;
        String details;
        String madeBy;
        Timestamp performedAt;
        if (record.value() instanceof DoctorCreatedMessage msg) {
            action = AuditAction.CREATE;
            details = "Kafka: Doctor Created: " + msg.doctorName();
            madeBy = msg.username();
            performedAt = msg.happenedAt();
        } else if (record.value() instanceof DoctorUpdatedMessage msg) {
            action = AuditAction.UPDATE;
            details = "Kafka: Doctor Updated: " + msg.doctorName();
            madeBy = msg.username();
            performedAt = msg.happenedAt();
        } else if (record.value() instanceof DoctorDeletedMessage msg) {
            action = AuditAction.DELETE;
            details = "Kafka: Doctor Deleted: " + msg.doctorName();
            madeBy = msg.username();
            performedAt = msg.happenedAt();
        } else {
            return; 
        }
        AuditLog auditLog = AuditLog.builder()
                .actionType(action)
                .madeBy(madeBy)
                .performedAt(performedAt)
                .details(details)
                .build();
        auditLogService.addAuditLog(auditLog);
    }
}