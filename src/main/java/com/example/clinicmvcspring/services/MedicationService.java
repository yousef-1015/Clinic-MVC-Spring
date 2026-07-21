package com.example.clinicmvcspring.services;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.clinicmvcspring.annotations.Audit;
import com.example.clinicmvcspring.models.AuditAction;
import com.example.clinicmvcspring.models.Medication;
import com.example.clinicmvcspring.repositories.MedicationRepo;

@Service
public class MedicationService {

    private final MedicationRepo repo;

    public MedicationService(MedicationRepo repo) {
        this.repo = repo;
    }

    @Audit(action = AuditAction.CREATE)
    public Medication addMedication(Medication med) {
        return repo.save(med);
    }

    public List<Medication> getAllMedications() {
        return repo.findAll();
    }

    public Optional<Medication> getMedicationByID(int id) {
        return repo.findById(id);
    }

    @Audit(action = AuditAction.DELETE)
    public void deleteMedication(Medication med) {
        repo.delete(med);
    }

    @Audit(action = AuditAction.DELETE)
    public void deleteMedicationByID(int id) {
        repo.deleteById(id);
    }

    @Audit(action = AuditAction.UPDATE)
    public Medication updateMedicationById(int id, Medication med) {
        med.setId(id);
        return repo.save(med);
    }

    public Page<Medication> getAllMedications(Pageable pageable) {
        return repo.findAll(pageable);
    }

}
