package com.example.clinicmvcspring.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.clinicmvcspring.dtos.PrescriptionMedicationDTO;
import com.example.clinicmvcspring.models.*;
import com.example.clinicmvcspring.repositories.*;

@Service
public class PrescriptionService {

    private final PrescriptionRepo repo;

    public PrescriptionService(PrescriptionRepo repo) {
        this.repo = repo;
    }

    public boolean addPrescription(Prescription pres) {
        return repo.insert(pres);
    }

    public List<Prescription> getAllPrescriptions() {
        return repo.findAll();
    }

    public Prescription getPrescriptionByID(int id) {
        return repo.getByID(id);
    }

    public boolean deletePrescription(Prescription pres) {
        return repo.delete(pres);
    }

    public boolean deletePrescriptionByID(int id) {
        return repo.delete(id);
    }

    public boolean updatePrescriptionById(int id, Prescription pres) {
        return repo.update(id, pres);
    }

    public List<PrescriptionMedicationDTO> getMedicationsForPrescription(int prescriptionId) {
        return repo.getMedicationsForPrescription(prescriptionId);
    }

    public List<Prescription> getAllPrescriptionsPaginated(int page, int size) {
        return repo.findAllPagination(page, size);
    }

    public int count() {
        return repo.count();
    }

}
