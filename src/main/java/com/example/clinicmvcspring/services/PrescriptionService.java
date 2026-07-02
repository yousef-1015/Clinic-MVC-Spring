package com.example.clinicmvcspring.services;

import java.util.List;
import java.util.Optional;

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

    public int addPrescription(Prescription pres) {
        return repo.insert(pres);
    }

    public List<Prescription> getAllPrescriptions() {
        return repo.findAll();
    }

    public Optional<Prescription> getPrescriptionByID(int id) {
        return repo.getByID(id);
    }

    public int deletePrescription(Prescription pres) {
        return repo.delete(pres);
    }

    public int deletePrescriptionByID(int id) {
        return repo.delete(id);
    }

    public int updatePrescriptionById(int id, Prescription pres) {
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
