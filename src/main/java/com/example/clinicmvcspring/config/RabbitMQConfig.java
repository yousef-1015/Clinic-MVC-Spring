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
        return QueueBuilder.durable("doctor.created.queue") // Make a durable (data not lost on restart)
                .withArgument("x-dead-letter-exchange", "clinic.dlx") // DLQ exchange
                .withArgument("x-dead-letter-routing-key", "dead.letter") // DLQ routing key
                .build();

    }

    @Bean
    public Queue doctorUpdatedQueue() {
        return QueueBuilder.durable("doctor.updated.queue")
                .withArgument("x-dead-letter-exchange", "clinic.dlx")
                .withArgument("x-dead-letter-routing-key", "dead.letter")
                .build();

    }

    @Bean
    public Queue doctorDeletedQueue() {
        return QueueBuilder.durable("doctor.deleted.queue")
                .withArgument("x-dead-letter-exchange", "clinic.dlx")
                .withArgument("x-dead-letter-routing-key", "dead.letter")
                .build();
    }

    @Bean
    public Queue userLoggedInQueue() {
        return QueueBuilder.durable("user.loggedin.queue")
                .withArgument("x-dead-letter-exchange", "clinic.dlx")
                .withArgument("x-dead-letter-routing-key", "dead.letter")
                .build();
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

    // THE GRAVEYARD EXCHANGE
    @Bean
    public TopicExchange deadLetterExchange() {
        return new TopicExchange("clinic.dlx"); // dlx = dead letter exchange
    }

    // THE GRAVEYARD QUEUE
    @Bean
    public Queue deadLetterQueue() {
        return new Queue("clinic.dlq", true); // dlq = dead letter queue
    }

    // BIND THE GRAVEYARD QUEUE TO THE GRAVEYARD EXCHANGE
    @Bean
    public Binding deadLetterBinding(Queue deadLetterQueue, TopicExchange deadLetterExchange) {
        return BindingBuilder
                .bind(deadLetterQueue)
                .to(deadLetterExchange)
                .with("dead.letter");// the routing key for dead letters
    }

}
