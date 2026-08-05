package com.example.clinicmvcspring.messaging.consumers;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class KafkaLoggingConsumer {

    @KafkaListener(topics = "doctor-events", groupId = "logging-group")
    public void handleDoctorEvent(ConsumerRecord<String, Object> record) {

        log.info("KAFKA LOG: | Topic: {} | Partition: {} | Offset: {} | Key(DoctorID): {} | Message: {}",
                record.topic(),
                record.partition(),
                record.offset(),
                record.key(),
                record.value().toString());

    }

}