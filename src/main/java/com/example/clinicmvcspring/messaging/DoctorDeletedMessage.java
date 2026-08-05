package com.example.clinicmvcspring.messaging;

import java.sql.Timestamp;

public record DoctorDeletedMessage (String doctorName, String message, String username, Timestamp happenedAt,int doctorID) {
    
}
