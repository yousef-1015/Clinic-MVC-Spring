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

    public boolean addPatient(PatientModel newPatient) {
        return repo.insertNewPatient(newPatient);// the job lies on the repo to access the DB
    }

    public List<PatientModel> getAllPatients() {
        return repo.getAllPatients();// the job lies on the repo to access the DB
    }

    public boolean deletePatient(PatientModel pat) {
        return repo.deletePatientFromDB(pat);
    }

    public PatientModel getPatientByID(int id) {
        return repo.getPatientByID(id);
    }

    public boolean deletePatientByID(int id) {
        return repo.deletePatientFromDB(id);
    }

    public boolean updatePatientById(int id, PatientModel pat) {
        return repo.updatePatient(id, pat);
    }

}