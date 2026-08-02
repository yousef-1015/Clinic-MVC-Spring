package com.example.clinicmvcspring.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.support.converter.JacksonJsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    // THE QUEUES
    @Bean
    public Queue doctorCreatedQueue() {
        return new Queue("doctor.created.queue", true);

    }

    @Bean
    public Queue doctorUpdatedQueue() {
        return new Queue("doctor.updated.queue", true);

    }

    @Bean
    public Queue doctorDeletedQueue() {
        return new Queue("doctor.deleted.queue", true);

    }

    @Bean
    public Queue userLoggedInQueue() {
        return new Queue("user.loggedIn.queue", true);

    }

    // THE EXCHANGE
    @Bean // broadcast
    public TopicExchange doctorExchange() {
        return new TopicExchange("clinic.events");
    }

    // THE BINDINGS
    @Bean
    public Binding doctorCreatedBinding(Queue doctorCreatedQueue, TopicExchange docExchange) {
        return BindingBuilder
                .bind(doctorCreatedQueue)
                .to(docExchange)
                .with("doctor.created");// the routing key
    }

    @Bean
    public Binding doctorUpdatedBinding(Queue doctorUpdatedQueue, TopicExchange docExchange) {
        return BindingBuilder
                .bind(doctorUpdatedQueue)
                .to(docExchange)
                .with("doctor.updated");// the routing key
    }

    @Bean
    public Binding doctorDeletedBinding(Queue doctorDeletedQueue, TopicExchange docExchange) {
        return BindingBuilder
                .bind(doctorDeletedQueue)
                .to(docExchange)
                .with("doctor.deleted");// the routing key
    }

    @Bean
    public Binding userLoggedInBinding(Queue userLoggedInQueue, TopicExchange docExchange) {
        return BindingBuilder
                .bind(userLoggedInQueue)
                .to(docExchange)
                .with("user.loggedin");// the routing key
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new JacksonJsonMessageConverter();
    }
}
