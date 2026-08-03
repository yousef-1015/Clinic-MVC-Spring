package com.example.clinicmvcspring.messaging;

import java.sql.Timestamp;

public record  DoctorUpdatedMessage (String doctorName, String message, String username, Timestamp happenedAt){
    
}
