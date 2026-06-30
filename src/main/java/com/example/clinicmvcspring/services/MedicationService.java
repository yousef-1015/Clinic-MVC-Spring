package com.example.clinicmvcspring.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.clinicmvcspring.models.*;
import com.example.clinicmvcspring.repositories.*;

@Service
public class MedicationService {

    private MedicationRepo repo;

    public MedicationService(MedicationRepo repo) {
        this.repo = repo;
    }

    public boolean addMedication(MedicationModel med) {
        return repo.insertNewMedication(med);
    }

    public List<MedicationModel> getAllMedications() {
        return repo.findAllMedications();
    }

    public MedicationModel getMedicationByID(int id) {
        return repo.getMedicationByID(id);
    }

    public boolean deleteMedication(MedicationModel med) {
        return repo.deleteMedicationFromDB(med);
    }

    public boolean deleteMedicationByID(int id) {
        return repo.deleteMedicationFromDB(id);
    }

    public boolean updateMedicationById(int id, MedicationModel med) {
        return repo.updateMedication(id, med);
    }

}
