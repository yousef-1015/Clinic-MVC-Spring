package com.example.clinicmvcspring.messaging;

import java.sql.Timestamp;

public record UserLoggedInMessage(String loggedInUsername, String message, Timestamp happenedAt) {

}
