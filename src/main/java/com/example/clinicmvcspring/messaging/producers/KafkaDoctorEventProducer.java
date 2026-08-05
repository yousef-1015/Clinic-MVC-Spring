package com.example.clinicmvcspring.messaging.producers;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import com.example.clinicmvcspring.messaging.DoctorCreatedMessage;
import com.example.clinicmvcspring.messaging.DoctorDeletedMessage;
import com.example.clinicmvcspring.messaging.DoctorUpdatedMessage;

@Component
public class KafkaDoctorEventProducer {

    private static final String TOPIC = "doctor-events";

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public KafkaDoctorEventProducer(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendDoctorCreated(DoctorCreatedMessage message) {

        kafkaTemplate.send(TOPIC, String.valueOf(message.doctorID()), message);
    }

    public void sendDoctorUpdated(DoctorUpdatedMessage message) {
        kafkaTemplate.send(TOPIC, String.valueOf(message.doctorID()), message);
    }

    public void sendDoctorDeleted(DoctorDeletedMessage message) {
        kafkaTemplate.send(TOPIC, String.valueOf(message.doctorID()), message);
    }
}
