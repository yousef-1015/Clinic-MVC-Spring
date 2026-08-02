package com.example.clinicmvcspring.messaging.producers;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import com.example.clinicmvcspring.messaging.DoctorCreatedMessage;
import com.example.clinicmvcspring.messaging.DoctorDeletedMessage;
import com.example.clinicmvcspring.messaging.DoctorUpdatedMessage;

@Component
public class DoctorEventProducer {

    private final RabbitTemplate rabbitTemplate;

    public DoctorEventProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendDoctorCreated(String name, String email) {

        DoctorCreatedMessage doctorCreatedMessage = new DoctorCreatedMessage(name, email);

        rabbitTemplate.convertAndSend("clinic.events", "doctor.created", doctorCreatedMessage);

    }

    public void sendDoctorUpdated(String name, String message) {

        DoctorUpdatedMessage doctorUpdatedMessage = new DoctorUpdatedMessage(name, message);

        rabbitTemplate.convertAndSend("clinic.events", "doctor.updated", doctorUpdatedMessage);

    }

    public void sendDoctorDeleted(String name, String message) {

        DoctorDeletedMessage doctorDeletedMessage = new DoctorDeletedMessage(name, message);

        rabbitTemplate.convertAndSend("clinic.events", "doctor.deleted", doctorDeletedMessage);

    }

}
