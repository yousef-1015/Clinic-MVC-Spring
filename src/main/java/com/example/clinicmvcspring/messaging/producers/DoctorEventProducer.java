package com.example.clinicmvcspring.messaging.producers;

import java.sql.Timestamp;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import com.example.clinicmvcspring.config.RabbitMQConstants;
import com.example.clinicmvcspring.messaging.DoctorCreatedMessage;
import com.example.clinicmvcspring.messaging.DoctorDeletedMessage;
import com.example.clinicmvcspring.messaging.DoctorUpdatedMessage;

@Component
public class DoctorEventProducer {

    private final RabbitTemplate rabbitTemplate;

    public DoctorEventProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendDoctorCreated(String name, String email, String username, Timestamp happendAt) {

        DoctorCreatedMessage doctorCreatedMessage = new DoctorCreatedMessage(name, email, username, happendAt);

        rabbitTemplate.convertAndSend(RabbitMQConstants.EXCHANGE_CLINIC_EVENTS,
                RabbitMQConstants.ROUTING_KEY_DOCTOR_CREATED, doctorCreatedMessage);

    }

    public void sendDoctorUpdated(String name, String message, String username, Timestamp happendAt) {

        DoctorUpdatedMessage doctorUpdatedMessage = new DoctorUpdatedMessage(name, message, username, happendAt);

        rabbitTemplate.convertAndSend(RabbitMQConstants.EXCHANGE_CLINIC_EVENTS,
                RabbitMQConstants.ROUTING_KEY_DOCTOR_UPDATED, doctorUpdatedMessage);

    }

    public void sendDoctorDeleted(String name, String message, String username, Timestamp happendAt) {

        DoctorDeletedMessage doctorDeletedMessage = new DoctorDeletedMessage(name, message, username, happendAt);

        rabbitTemplate.convertAndSend(RabbitMQConstants.EXCHANGE_CLINIC_EVENTS,
                RabbitMQConstants.ROUTING_KEY_DOCTOR_DELETED, doctorDeletedMessage);

    }

}
