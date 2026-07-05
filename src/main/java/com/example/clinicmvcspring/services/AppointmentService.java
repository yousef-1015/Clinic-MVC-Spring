package com.example.clinicmvcspring.services;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.example.clinicmvcspring.models.*;
import com.example.clinicmvcspring.repositories.*;

@Service
public class AppointmentService {

    private final AppointmentRepo repo;

    // spring boot will automatically do the dependency injection
    public AppointmentService(AppointmentRepo repo) {
        this.repo = repo;
    }

    public Appointment addAppointment(Appointment app) {
        return repo.save(app);
    }

    public List<Appointment> getAllAppointments() {
        return repo.findAll();
    }

    public Optional<Appointment> getAppointmentByID(int id) {
        return repo.findById(id);
    }

    public void deleteAppointment(Appointment app) {
        repo.delete(app);
    }

    public void deleteAppointmentByID(int id) {
        repo.deleteById(id);
    }

    public Appointment updateAppointmentById(int id, Appointment app) {
        app.setId(id);
        return repo.save(app);

    }

    public List<Appointment> getAllAppointments(int page, int size) {
        return repo.findAll(PageRequest.of(page, size)).getContent();
    }

    public long countAppointments() {
        return repo.count();
    }

}
