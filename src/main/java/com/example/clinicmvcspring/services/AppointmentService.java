package com.example.clinicmvcspring.services;

import java.util.List;

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

    public boolean addAppointment(Appointment app) {
        return repo.insert(app);
    }

    public List<Appointment> getAllAppointments() {
        return repo.findAll();
    }

    public Appointment getAppointmentByID(int id) {
        return repo.getByID(id);
    }

    public boolean deleteAppointment(Appointment app) {
        return repo.delete(app);
    }

    public boolean deleteAppointmentByID(int id) {
        return repo.delete(id);
    }

    public boolean updateAppointmentById(int id, Appointment app) {
        return repo.update(id, app);
    }

}
