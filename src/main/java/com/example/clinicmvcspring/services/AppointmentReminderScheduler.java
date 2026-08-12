package com.example.clinicmvcspring.services;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.example.clinicmvcspring.models.Appointment;
import com.example.clinicmvcspring.models.AppointmentStatus;
import com.example.clinicmvcspring.repositories.AppointmentRepo;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class AppointmentReminderScheduler {

    private final AppointmentRepo appointmentRepo;
    private final EmailService emailService;

    public AppointmentReminderScheduler(AppointmentRepo appointmentRepo, EmailService emailService) {
        this.appointmentRepo = appointmentRepo;
        this.emailService = emailService;
    }

    // Runs every minute for testing: "0 */1 * * * ?"
    // change to midnight: "0 0 0 * * ?"
    @Scheduled(cron = "0 0 0 * * ?")
    public void sendDailyAppointmentReminders() {
        log.info("Starting scheduled daily appointment reminder job...");

        // Calculate tomorrow's start (00:00:00) and end (23:59:59)
        LocalDate tomorrow = LocalDate.now().plusDays(1);
        Timestamp startOfTomorrow = Timestamp.valueOf(LocalDateTime.of(tomorrow, LocalTime.MIN));
        Timestamp endOfTomorrow = Timestamp.valueOf(LocalDateTime.of(tomorrow, LocalTime.MAX));

        List<Appointment> upcomingAppointments = appointmentRepo.findUpcomingAppointments(
                AppointmentStatus.Scheduled,
                startOfTomorrow,
                endOfTomorrow);

        log.info("Found {} upcoming appointments for tomorrow ({})", upcomingAppointments.size(), tomorrow);

        for (Appointment appointment : upcomingAppointments) {
            if (appointment.getPatient() != null && appointment.getPatient().getEmail() != null) {
                emailService.sendAppointmentReminder(
                        appointment.getPatient().getEmail(),
                        appointment.getPatient().getFirstName() + " " + appointment.getPatient().getLastName(),
                        appointment.getDoctor() != null
                                ? appointment.getDoctor().getFirstName() + " " + appointment.getDoctor().getLastName()
                                : "Staff",
                        appointment.getDateAndTime());
            }
        }
    }
}
