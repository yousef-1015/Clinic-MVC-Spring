package com.example.clinicmvcspring.services;

import java.sql.Timestamp;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.clinicmvcspring.annotations.Audit;
import com.example.clinicmvcspring.dtos.AppointmentDTO;
import com.example.clinicmvcspring.mappers.AppointmentMapper;
import com.example.clinicmvcspring.models.Appointment;
import com.example.clinicmvcspring.models.AppointmentStatus;
import com.example.clinicmvcspring.models.AuditAction;
import com.example.clinicmvcspring.repositories.AppointmentRepo;
import com.example.clinicmvcspring.specifications.AppointmentSpecification;

@Service
public class AppointmentService {

    private final AppointmentRepo repo;
    private final AppointmentMapper mapper;

    // spring boot will automatically do the dependency injection
    public AppointmentService(AppointmentRepo repo, AppointmentMapper mapper) {
        this.repo = repo;
        this.mapper = mapper;
    }

    @Audit(action = AuditAction.CREATE)
    public AppointmentDTO addAppointment(Appointment app) {
        return mapper.appointmentToAppointmentDTO(repo.save(app));
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

}
