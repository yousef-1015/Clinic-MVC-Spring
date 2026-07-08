package com.example.clinicmvcspring.services;

import java.sql.Timestamp;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.example.clinicmvcspring.models.*;
import com.example.clinicmvcspring.repositories.*;
import com.example.clinicmvcspring.specifications.AppointmentSpecification;
import com.example.clinicmvcspring.dtos.AppointmentDTO;
import com.example.clinicmvcspring.mappers.AppointmentMapper;

import org.springframework.transaction.annotation.Transactional;

@Service
public class AppointmentService {

    private final AppointmentRepo repo;
    private final AppointmentMapper mapper;

    // spring boot will automatically do the dependency injection
    public AppointmentService(AppointmentRepo repo, AppointmentMapper mapper) {
        this.repo = repo;
        this.mapper = mapper;
    }

    public AppointmentDTO addAppointment(Appointment app) {
        return mapper.appointmentToAppointmentDTO(repo.save(app));
    }

    public List<AppointmentDTO> getAllAppointments() {
        return repo.findAll().stream().map(app -> mapper.appointmentToAppointmentDTO(app)).toList();
    }

    public Optional<AppointmentDTO> getAppointmentByID(int id) {
        return repo.findById(id).map(app -> mapper.appointmentToAppointmentDTO(app));
    }

    public Optional<Appointment> getAppointmentEntityByID(int id) {
        return repo.findById(id);
    }

    public void deleteAppointment(Appointment app) {
        repo.delete(app);
    }

    public void deleteAppointmentByID(int id) {
        repo.deleteById(id);
    }

    public AppointmentDTO updateAppointmentById(int id, Appointment app) {
        app.setId(id);
        return mapper.appointmentToAppointmentDTO(repo.save(app));

    }

    public Page<AppointmentDTO> getAllAppointments(Pageable pageable) {
        Page<Appointment> appointmentPage = repo.findAll(pageable);
        return appointmentPage.map(app -> mapper.appointmentToAppointmentDTO(app));// convert to the DTO
    }

    @Transactional
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
