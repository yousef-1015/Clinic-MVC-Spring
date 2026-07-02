package com.example.clinicmvcspring.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.clinicmvcspring.models.*;
import com.example.clinicmvcspring.repositories.*;

@Service
public class PatientService {
    private final PatientRepo repo;

    public PatientService(PatientRepo repo) {
        this.repo = repo;
    }

    public int addPatient(Patient newPatient) {
        return repo.insert(newPatient);// the job lies on the repo to access the DB
    }

    public List<Patient> getAllPatients() {
        return repo.getAll();// the job lies on the repo to access the DB
    }

    public int deletePatient(Patient pat) {
        return repo.delete(pat);
    }

    public Patient getPatientByID(int id) {
        return repo.getByID(id);
    }

    public int deletePatientByID(int id) {
        return repo.delete(id);
    }

    public int updatePatientById(int id, Patient pat) {
        return repo.update(id, pat);
    }

    public List<Patient> getAllPatients(int page, int size) {
        return repo.findAllPagination(page, size);
    }

    public int countPatients() {
        return repo.count();
    }

}
