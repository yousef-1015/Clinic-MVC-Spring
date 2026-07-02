package com.example.clinicmvcspring.services;

import java.util.List;
import java.util.Optional;

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

    public int addAppointment(Appointment app) {
        return repo.insert(app);
    }

    public List<Appointment> getAllAppointments() {
        return repo.findAll();
    }

    public Optional<Appointment> getAppointmentByID(int id) {
        return repo.findByID(id);
    }

    public int deleteAppointment(Appointment app) {
        return repo.delete(app);
    }

    public int deleteAppointmentByID(int id) {
        return repo.delete(id);
    }

    public int updateAppointmentById(int id, Appointment app) {
        return repo.update(id, app);
    }

    public List<Appointment> getAllAppointments(int page, int size) {
        return repo.findAllPagination(page, size);
    }

    public int countAppointments() {
        return repo.count();
    }

}
