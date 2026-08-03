package com.example.clinicmvcspring.messaging.producers;

import java.sql.Timestamp;

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


    public void sendUserLoggedIn(String username, String message,Timestamp happendAt) {

        UserLoggedInMessage userLoggedInMessage = new UserLoggedInMessage(username, message,happendAt);

        rabbitTemplate.convertAndSend(RabbitMQConstants.EXCHANGE_CLINIC_EVENTS, RabbitMQConstants.ROUTING_KEY_USER_LOGGEDIN, userLoggedInMessage);

    }
}
