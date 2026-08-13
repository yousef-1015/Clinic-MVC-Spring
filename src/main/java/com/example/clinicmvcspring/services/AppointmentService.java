package com.example.clinicmvcspring.services;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.clinicmvcspring.annotations.Audit;
import com.example.clinicmvcspring.config.RabbitMQConstants;
import com.example.clinicmvcspring.dtos.AppointmentDTO;
import com.example.clinicmvcspring.mappers.AppointmentMapper;
import com.example.clinicmvcspring.models.Appointment;
import com.example.clinicmvcspring.models.AppointmentStatus;
import com.example.clinicmvcspring.models.AuditAction;
import com.example.clinicmvcspring.repositories.AppointmentRepo;
import com.example.clinicmvcspring.specifications.AppointmentSpecification;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class AppointmentService {

    private final AppointmentRepo repo;
    private final AppointmentMapper mapper;
    private final RabbitTemplate rabbitTemplate;

    // spring boot will automatically do the dependency injection
    public AppointmentService(AppointmentRepo repo, AppointmentMapper mapper,
            RabbitTemplate rabbitTemplate) {
        this.repo = repo;
        this.mapper = mapper;
        this.rabbitTemplate = rabbitTemplate;
    }

    @Audit(action = AuditAction.CREATE)
    public AppointmentDTO addAppointment(Appointment app) {
        if (!repo.isDoctorBooked(app.getDoctor(), app.getDateAndTime())) {
            return mapper.appointmentToAppointmentDTO(repo.save(app));
        } else {
            throw new IllegalArgumentException("The doctor is already booked at this time!");
        }

    }

    public List<AppointmentDTO> getAllAppointments() {
        return repo.findAll().stream().map(app -> mapper.appointmentToAppointmentDTO(app)).toList();
    }

    @Cacheable(value = "Appointments", key = "#id")
    public Optional<AppointmentDTO> getAppointmentByID(int id) {
        return repo.findById(id).map(app -> mapper.appointmentToAppointmentDTO(app));
    }

    public Optional<Appointment> getAppointmentEntityByID(int id) {
        return repo.findById(id);
    }

    @Audit(action = AuditAction.DELETE)
    @CacheEvict(value = "Appointments", key = "#app.getId()")
    public void deleteAppointment(Appointment app) {
        repo.delete(app);
    }

    @Audit(action = AuditAction.DELETE)
    @CacheEvict(value = "Appointments", key = "#id")
    public void deleteAppointmentByID(int id) {
        repo.deleteById(id);
    }

    @Audit(action = AuditAction.UPDATE)
    @CachePut(value = "Appointments", key = "#id")
    public AppointmentDTO updateAppointmentById(int id, Appointment app) {
        app.setId(id);
        return mapper.appointmentToAppointmentDTO(repo.save(app));

    }

    public Page<AppointmentDTO> getAllAppointments(Pageable pageable) {
        Page<Appointment> appointmentPage = repo.findAll(pageable);
        return appointmentPage.map(app -> mapper.appointmentToAppointmentDTO(app));// convert to the DTO
    }

    @Audit(action = AuditAction.UPDATE)
    @Transactional
    @CacheEvict(value = "Appointments", key = "#appointmentId")
    public void transferPatient(int appointmentId, int doctorId) {
        Optional<Appointment> appOpt = repo.findById(appointmentId);

        if (appOpt.isEmpty()) {
            throw new NoSuchElementException("Appointment not found with id: " + appointmentId);
        }

        Appointment app = appOpt.get();// Managed Entity
        app.setDoctorId(doctorId);// The Managed Entity is now DIRTY

        // Because of the @Transactional annotation, hibernate saved the appointment
        // automatically
        // repo.save(app);
    }// app is now a DETACHED entity

    public Page<AppointmentDTO> findAppointmentByStatus(AppointmentStatus status, Pageable pageable) {
        Page<Appointment> appointmentPage = repo.findByStatus(status, pageable);

        return appointmentPage.map(app -> mapper.appointmentToAppointmentDTO(app));
    }

    public Page<AppointmentDTO> findAppointmentByDate(Timestamp start, Timestamp end, Pageable pageable) {

        Specification<Appointment> spec = Specification.where(AppointmentSpecification.isBetweenDates(start, end));

        Page<Appointment> appointmentPage = repo.findAll(spec, pageable);

        return appointmentPage.map(app -> mapper.appointmentToAppointmentDTO(app));
    }

    @Audit(action = AuditAction.UPDATE)
    public AppointmentDTO cancelAppointment(int id) {
        Appointment appointmentToCancel = repo.findById(id)
                .orElseThrow(() -> new NoSuchElementException("No Appointment found with the Id" + id));
        if (appointmentToCancel.getStatus() != AppointmentStatus.Scheduled) {
            throw new IllegalStateException(
                    "Cannot cancel appointment because it is already " + appointmentToCancel.getStatus());
        }
        appointmentToCancel.setStatus(AppointmentStatus.Cancelled);
        return mapper.appointmentToAppointmentDTO(repo.save(appointmentToCancel));
    }

    @Audit(action = AuditAction.UPDATE)
    public AppointmentDTO completeAppointment(int id) {
        Appointment appointmentToComplete = repo.findById(id)
                .orElseThrow(() -> new NoSuchElementException("No Appointment found with the Id " + id));

        if (appointmentToComplete.getStatus() != AppointmentStatus.Scheduled) {
            throw new IllegalStateException(
                    "Cannot complete appointment because it is already " + appointmentToComplete.getStatus());
        }

        appointmentToComplete.setStatus(AppointmentStatus.Completed);
        Appointment savedApp = repo.save(appointmentToComplete);

        // THIS IS THE PRODUCER, send message to rabbitmq
        rabbitTemplate.convertAndSend(
                RabbitMQConstants.EXCHANGE_CLINIC_EVENTS,
                RabbitMQConstants.ROUTING_KEY_APPOINTMENT_COMPLETED,
                savedApp.getId());
        return mapper.appointmentToAppointmentDTO(savedApp);
    }

    @Scheduled(cron = "0 0 0 * * *")
    public void cleanupOldAppointments() {
        LocalDateTime oneWeekAgoDate = LocalDateTime.now().minusWeeks(1);
        Timestamp cutoffTimestamp = Timestamp.valueOf(oneWeekAgoDate);

        List<AppointmentStatus> statusesToDelete = List.of(
                AppointmentStatus.Completed,
                AppointmentStatus.Cancelled);

        repo.deleteOldAppointments(statusesToDelete, cutoffTimestamp);
    }

}
