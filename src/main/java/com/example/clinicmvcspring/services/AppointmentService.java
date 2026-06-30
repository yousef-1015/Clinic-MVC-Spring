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

    public boolean addAppointment(AppointmentModel app) {
        return repo.insertNewAppointment(app);
    }

    public List<AppointmentModel> getAllAppointments() {
        return repo.findAllAppointments();
    }

    public AppointmentModel getAppointmentByID(int id) {
        return repo.getAppointmentByID(id);
    }

    public boolean deleteAppointment(AppointmentModel app) {
        return repo.deleteAppointmentFromDB(app);
    }

    public boolean deleteAppointmentByID(int id) {
        return repo.deleteAppointmentFromDB(id);
    }

    public boolean updateAppointmentById(int id, AppointmentModel app) {
        return repo.updateAppointment(id, app);
    }

}
