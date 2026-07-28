package com.example.clinicmvcspring.events;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserLoggedInEvent {

    private final String loggedInUsername;
    private final String message;

}
