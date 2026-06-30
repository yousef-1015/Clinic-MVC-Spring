package com.example.clinicmvcspring.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.clinicmvcspring.models.*;
import com.example.clinicmvcspring.repositories.*;

@Service
public class PrescriptionService {

    private PrescriptionRepo repo;

    public PrescriptionService(PrescriptionRepo repo) {
        this.repo = repo;
    }

    public boolean addPrescription(PrescriptionModel pres) {
        return repo.insertNewPrescription(pres);
    }

    public List<PrescriptionModel> getAllPrescriptions() {
        return repo.findAllPrescriptions();
    }

    public PrescriptionModel getPrescriptionByID(int id) {
        return repo.getPrescriptionByID(id);
    }

    public boolean deletePrescription(PrescriptionModel pres) {
        return repo.deletePrescriptionFromDB(pres);
    }

    public boolean deletePrescriptionByID(int id) {
        return repo.deletePrescriptionFromDB(id);
    }

    public boolean updatePrescriptionById(int id, PrescriptionModel pres) {
        return repo.updatePrescription(id, pres);
    }

}
