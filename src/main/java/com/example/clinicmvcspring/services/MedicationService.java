package com.example.clinicmvcspring.services;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.example.clinicmvcspring.models.*;
import com.example.clinicmvcspring.repositories.*;

@Service
public class MedicationService {

    private final MedicationRepo repo;

    public MedicationService(MedicationRepo repo) {
        this.repo = repo;
    }

    public Medication addMedication(Medication med) {
        return repo.save(med);
    }

    public List<Medication> getAllMedications() {
        return repo.findAll();
    }

    public Optional<Medication> getMedicationByID(int id) {
        return repo.findById(id);
    }

    public void deleteMedication(Medication med) {
        repo.delete(med);
    }

    public void deleteMedicationByID(int id) {
        repo.deleteById(id);
    }

    public Medication updateMedicationById(int id, Medication med) {
        med.setId(id);
        return repo.save(med);
    }

    public List<Medication> getAllMedications(int page, int size) {
        return repo.findAll(PageRequest.of(page, size)).getContent();
    }

    public long countMedications() {
        return repo.count();
    }
}
