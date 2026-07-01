package com.example.clinicmvcspring.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.clinicmvcspring.models.*;
import com.example.clinicmvcspring.repositories.*;

@Service
public class MedicationService {

    private final MedicationRepo repo;

    public MedicationService(MedicationRepo repo) {
        this.repo = repo;
    }

    public boolean addMedication(Medication med) {
        return repo.insert(med);
    }

    public List<Medication> getAllMedications() {
        return repo.findAll();
    }

    public Medication getMedicationByID(int id) {
        return repo.findByID(id);
    }

    public boolean deleteMedication(Medication med) {
        return repo.delete(med);
    }

    public boolean deleteMedicationByID(int id) {
        return repo.delete(id);
    }

    public boolean updateMedicationById(int id, Medication med) {
        return repo.update(id, med);
    }

    public List<Medication> getAllMedications(int page, int size) {
        return repo.findAllPagination(page, size);
    }

    public int countMedications() {
        return repo.count();
    }
}
