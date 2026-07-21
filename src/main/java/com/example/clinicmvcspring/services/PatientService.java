package com.example.clinicmvcspring.services;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.clinicmvcspring.annotations.Audit;
import com.example.clinicmvcspring.models.AuditAction;
import com.example.clinicmvcspring.models.Patient;
import com.example.clinicmvcspring.repositories.PatientRepo;

@Service
public class PatientService {
    private final PatientRepo repo;

    public PatientService(PatientRepo repo) {
        this.repo = repo;
    }

    @Audit(action = AuditAction.CREATE)
    public Patient addPatient(Patient newPatient) {
        return repo.save(newPatient);// the job lies on the repo to access the DB
    }

    public List<Patient> getAllPatients() {
        return repo.findAll();// the job lies on the repo to access the DB
    }

    @Audit(action = AuditAction.DELETE)
    public void deletePatient(Patient pat) {
        repo.delete(pat);
    }

    public Optional<Patient> getPatientByID(int id) {
        return repo.findById(id);
    }

    @Audit(action = AuditAction.DELETE)
    public void deletePatientByID(int id) {
        repo.deleteById(id);
    }

    @Audit(action = AuditAction.UPDATE)
    public Patient updatePatientById(int id, Patient pat) {
        pat.setId(id);
        return repo.save(pat);
    }

    public Page<Patient> getAllPatients(Pageable pageable) {
        return repo.findAll(pageable);
    }

}
