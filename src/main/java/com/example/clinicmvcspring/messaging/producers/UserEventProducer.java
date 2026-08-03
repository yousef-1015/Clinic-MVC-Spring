package com.example.clinicmvcspring.messaging.producers;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import com.example.clinicmvcspring.config.RabbitMQConstants;
import com.example.clinicmvcspring.messaging.UserLoggedInMessage;

@Component
public class UserEventProducer {
    private final RabbitTemplate rabbitTemplate;

    public UserEventProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }


    public void sendUserLoggedIn(String username, String message) {

        UserLoggedInMessage userLoggedInMessage = new UserLoggedInMessage(username, message);

        rabbitTemplate.convertAndSend(RabbitMQConstants.EXCHANGE_CLINIC_EVENTS, RabbitMQConstants.ROUTING_KEY_USER_LOGGEDIN, userLoggedInMessage);

    }
}
