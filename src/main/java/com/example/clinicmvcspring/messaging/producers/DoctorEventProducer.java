package com.example.clinicmvcspring.messaging.producers;

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

    public void sendDoctorCreated(DoctorCreatedMessage message) {

        rabbitTemplate.convertAndSend(RabbitMQConstants.EXCHANGE_CLINIC_EVENTS,
                RabbitMQConstants.ROUTING_KEY_DOCTOR_CREATED, message);

    }

    public void sendDoctorUpdated(DoctorUpdatedMessage message) {

        rabbitTemplate.convertAndSend(RabbitMQConstants.EXCHANGE_CLINIC_EVENTS,
                RabbitMQConstants.ROUTING_KEY_DOCTOR_UPDATED, message);

    }

    public void sendDoctorDeleted(DoctorDeletedMessage message) {

        rabbitTemplate.convertAndSend(RabbitMQConstants.EXCHANGE_CLINIC_EVENTS,
                RabbitMQConstants.ROUTING_KEY_DOCTOR_DELETED, message);

    }

}
