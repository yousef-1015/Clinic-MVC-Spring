package com.example.clinicmvcspring.messaging;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
public class UserEventProducer {
    private final RabbitTemplate rabbitTemplate;

    public UserEventProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }


    public void sendUserLoggedIn(String username, String message) {

        UserLoggedInMessage userLoggedInMessage = new UserLoggedInMessage(username, message);

        rabbitTemplate.convertAndSend("clinic.events", "user.loggedin", userLoggedInMessage);

    }
}
