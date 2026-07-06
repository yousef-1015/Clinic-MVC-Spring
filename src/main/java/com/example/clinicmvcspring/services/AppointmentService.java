package com.example.clinicmvcspring.services;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.example.clinicmvcspring.models.*;
import com.example.clinicmvcspring.repositories.*;
import com.example.clinicmvcspring.dtos.AppointmentDTO;
import com.example.clinicmvcspring.dtos.PrescriptionDTO;

import org.springframework.transaction.annotation.Transactional;

@Service
public class AppointmentService {

    private final AppointmentRepo repo;

    // spring boot will automatically do the dependency injection
    public AppointmentService(AppointmentRepo repo) {
        this.repo = repo;
    }

    public AppointmentDTO addAppointment(Appointment app) {
        return convertToDTO(repo.save(app));
    }

    public List<AppointmentDTO> getAllAppointments() {
        return repo.findAll().stream().map(app -> convertToDTO(app)).toList();
    }

    public Optional<AppointmentDTO> getAppointmentByID(int id) {
        return repo.findById(id).map(app -> convertToDTO(app));
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
        return convertToDTO(repo.save(app));

    }

    public List<AppointmentDTO> getAllAppointments(int page, int size) {
        return repo.findAll(PageRequest.of(page, size)).getContent().stream().map(app -> convertToDTO(app)).toList();
    }

    public long countAppointments() {
        return repo.count();
    }

    public AppointmentDTO convertToDTO(Appointment app) {
        PrescriptionDTO presDto = null;

        if (app.getPrescription() != null) {
            presDto = new PrescriptionDTO(
                    app.getPrescription().getId(),
                    app.getPrescription().getPrescriptionNotes(),
                    app.getPrescription().getCreatedAt());
        }

        return new AppointmentDTO(
                app.getId(),
                app.getDateAndTime(),
                app.getPatient() != null ? app.getPatient().getId() : 0,
                app.getDoctor() != null ? app.getDoctor().getId() : 0,
                app.getStatus(),
                app.getCreatedAt(),
                presDto);
    }

    @Transactional
    public void transferPatient(int appointmentId, int doctorId) {
        Optional<Appointment> appOpt = repo.findById(appointmentId);

        if (appOpt.isEmpty()) {
            throw new NoSuchElementException("Appointment not found with id: " + appointmentId);
        }

        Appointment app = appOpt.get();
        app.setDoctorId(doctorId);

        repo.save(app);
    }

}
