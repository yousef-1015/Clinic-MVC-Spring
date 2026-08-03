package com.example.clinicmvcspring.messaging;

import java.sql.Timestamp;

public record DoctorCreatedMessage(String doctorName, String doctorEmail, String username, Timestamp happenedAt) {

}
