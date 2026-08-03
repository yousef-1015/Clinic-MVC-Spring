package com.example.clinicmvcspring.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
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
        return QueueBuilder.durable(RabbitMQConstants.QUEUE_DOCTOR_CREATED) // Make a durable (data not lost on restart)
                .withArgument("x-dead-letter-exchange", RabbitMQConstants.EXCHANGE_DEAD_LETTER) // DLQ exchange
                .withArgument("x-dead-letter-routing-key", RabbitMQConstants.ROUTING_KEY_DEAD_LETTER) // DLQ routing key
                .build();

    }

    @Bean
    public Queue doctorUpdatedQueue() {
        return QueueBuilder.durable(RabbitMQConstants.QUEUE_DOCTOR_UPDATED)
                .withArgument("x-dead-letter-exchange", RabbitMQConstants.EXCHANGE_DEAD_LETTER)
                .withArgument("x-dead-letter-routing-key", RabbitMQConstants.ROUTING_KEY_DEAD_LETTER)
                .build();

    }

    @Bean
    public Queue doctorDeletedQueue() {
        return QueueBuilder.durable(RabbitMQConstants.QUEUE_DOCTOR_DELETED)
                .withArgument("x-dead-letter-exchange", RabbitMQConstants.EXCHANGE_DEAD_LETTER)
                .withArgument("x-dead-letter-routing-key", RabbitMQConstants.ROUTING_KEY_DEAD_LETTER)
                .build();
    }

    @Bean
    public Queue userLoggedInQueue() {
        return QueueBuilder.durable(RabbitMQConstants.QUEUE_USER_LOGGEDIN)
                .withArgument("x-dead-letter-exchange", RabbitMQConstants.EXCHANGE_DEAD_LETTER)
                .withArgument("x-dead-letter-routing-key", RabbitMQConstants.ROUTING_KEY_DEAD_LETTER)
                .build();
    }

    // THE EXCHANGE
    @Bean // broadcast
    public TopicExchange doctorExchange() {
        return new TopicExchange(RabbitMQConstants.EXCHANGE_CLINIC_EVENTS);
    }

    // THE BINDINGS
    @Bean
    public Binding doctorCreatedBinding(Queue doctorCreatedQueue, TopicExchange doctorExchange) {
        return BindingBuilder
                .bind(doctorCreatedQueue)
                .to(doctorExchange)
                .with(RabbitMQConstants.ROUTING_KEY_DOCTOR_CREATED);// the routing key
    }

    @Bean
    public Binding doctorUpdatedBinding(Queue doctorUpdatedQueue, TopicExchange doctorExchange) {
        return BindingBuilder
                .bind(doctorUpdatedQueue)
                .to(doctorExchange)
                .with(RabbitMQConstants.ROUTING_KEY_DOCTOR_UPDATED);// the routing key
    }

    @Bean
    public Binding doctorDeletedBinding(Queue doctorDeletedQueue, TopicExchange doctorExchange) {
        return BindingBuilder
                .bind(doctorDeletedQueue)
                .to(doctorExchange)
                .with(RabbitMQConstants.ROUTING_KEY_DOCTOR_DELETED);// the routing key
    }

    @Bean
    public Binding userLoggedInBinding(Queue userLoggedInQueue, TopicExchange doctorExchange) {
        return BindingBuilder
                .bind(userLoggedInQueue)
                .to(doctorExchange)
                .with(RabbitMQConstants.ROUTING_KEY_USER_LOGGEDIN);// the routing key
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new JacksonJsonMessageConverter();
    }

    // THE GRAVEYARD EXCHANGE
    @Bean
    public TopicExchange deadLetterExchange() {
        return new TopicExchange(RabbitMQConstants.EXCHANGE_DEAD_LETTER); // dlx = dead letter exchange
    }

    // THE GRAVEYARD QUEUE
    @Bean
    public Queue deadLetterQueue() {
        return new Queue(RabbitMQConstants.QUEUE_DEAD_LETTER, true); // dlq = dead letter queue
    }

    // BIND THE GRAVEYARD QUEUE TO THE GRAVEYARD EXCHANGE
    @Bean
    public Binding deadLetterBinding(Queue deadLetterQueue, TopicExchange deadLetterExchange) {
        return BindingBuilder
                .bind(deadLetterQueue)
                .to(deadLetterExchange)
                .with(RabbitMQConstants.ROUTING_KEY_DEAD_LETTER);// the routing key for dead letters
    }

}
