package com.example.clinicmvcspring.messaging;

import java.sql.Timestamp;

public record DoctorCreatedMessage(String doctorName, String doctorEmail, String username, Timestamp happenedAt,int doctorID) {
// DOCTOR ID IS THE KEY FOR KAFKA
}
