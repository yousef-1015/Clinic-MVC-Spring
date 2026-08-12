package com.example.clinicmvcspring.services;

import java.sql.Timestamp;

public interface EmailService {
    void sendAppointmentReminder(String toEmail, String patientName, String doctorName, Timestamp appointmentTime);
}
