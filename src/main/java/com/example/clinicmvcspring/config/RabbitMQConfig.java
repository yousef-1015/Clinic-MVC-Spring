package com.example.clinicmvcspring.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.support.converter.JacksonJsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    // THE QUEUE
    @Bean
    public Queue doctorQueue() {
        return new Queue("doctor.queue", true);
    }

    // THE EXCHANGE
    @Bean // broadcast
    public FanoutExchange doctorExchange() {
        return new FanoutExchange("doctor.exchange");
    }

    // THE BINDING
    @Bean
    public Binding binding(Queue docQueue, FanoutExchange docExchange) {
        return BindingBuilder
                .bind(docQueue)
                .to(docExchange);
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new JacksonJsonMessageConverter();
    }
}
