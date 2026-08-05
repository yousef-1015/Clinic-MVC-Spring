package com.example.clinicmvcspring.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaConfig {

    @Bean
    public NewTopic createNewTopic() {
        return TopicBuilder.name("doctor-events")
                .partitions(3) // one for each action Type
                .replicas(1)
                .build();
    }



}
