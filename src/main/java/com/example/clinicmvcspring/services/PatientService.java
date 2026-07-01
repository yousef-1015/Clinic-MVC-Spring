package com.example.clinicmvcspring.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.clinicmvcspring.models.*;
import com.example.clinicmvcspring.repositories.*;

@Service
public class PatientService {
    private PatientRepo repo;

    public PatientService(PatientRepo repo) {
        this.repo = repo;
    }

    public boolean addPatient(Patient newPatient) {
        return repo.insert(newPatient);// the job lies on the repo to access the DB
    }

    public List<Patient> getAllPatients() {
        return repo.getAll();// the job lies on the repo to access the DB
    }

    public boolean deletePatient(Patient pat) {
        return repo.delete(pat);
    }

    public Patient getPatientByID(int id) {
        return repo.getByID(id);
    }

    public boolean deletePatientByID(int id) {
        return repo.delete(id);
    }

    public boolean updatePatientById(int id, Patient pat) {
        return repo.update(id, pat);
    }

}