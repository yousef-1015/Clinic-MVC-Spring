package com.example.clinicmvcspring.services;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.example.clinicmvcspring.models.*;
import com.example.clinicmvcspring.repositories.*;

@Service
public class PatientService {
    private final PatientRepo repo;

    public PatientService(PatientRepo repo) {
        this.repo = repo;
    }

    public Patient addPatient(Patient newPatient) {
        return repo.save(newPatient);// the job lies on the repo to access the DB
    }

    public List<Patient> getAllPatients() {
        return repo.findAll();// the job lies on the repo to access the DB
    }

    public void deletePatient(Patient pat) {
        repo.delete(pat);
    }

    public Optional<Patient> getPatientByID(int id) {
        return repo.findById(id);
    }

    public void deletePatientByID(int id) {
        repo.deleteById(id);
    }

    public Patient updatePatientById(int id, Patient pat) {
        pat.setId(id);
        return repo.save(pat);
    }

    public List<Patient> getAllPatients(int page, int size) {
        return repo.findAll(PageRequest.of(page, size)).getContent();
    }

    public long countPatients() {
        return repo.count();
    }

}
