package com.example.clinicmvcspring.services;

import java.sql.Timestamp;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;

    public EmailServiceImpl(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Override
    @Async
    public void sendAppointmentReminder(String toEmail, String patientName, String doctorName,
            Timestamp appointmentTime) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(toEmail);
            message.setSubject("Reminder: Upcoming Clinic Appointment Tomorrow");
            message.setText("Hello " + patientName + ",\n\n" +
                    "This is a reminder for your upcoming appointment with Dr. " + doctorName +
                    " scheduled for " + appointmentTime + ".\n\n" +
                    "Please arrive 10 minutes before your appointment time.\n\n" +
                    "Best regards,\nClinic Management Team");

            log.info("Sending reminder email asynchronously to {} for appointment at {}", toEmail, appointmentTime);

            // Uncomment when ready to send via SMTP:
            mailSender.send(message);

            log.info("Email successfully sent to {}", toEmail);
        } catch (Exception e) {
            log.error("Failed to send appointment reminder email to {}: {}", toEmail, e.getMessage());
        }
    }
}
