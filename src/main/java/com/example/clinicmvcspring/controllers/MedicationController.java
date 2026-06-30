package com.example.clinicmvcspring.controllers;

import org.springframework.web.bind.annotation.RestController;

import com.example.clinicmvcspring.models.MedicationModel;
import com.example.clinicmvcspring.services.MedicationService;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
@RequestMapping("/api/v1/medications")
public class MedicationController {
    private final MedicationService medicationService;

    public MedicationController(MedicationService medicationService) {
        this.medicationService = medicationService;
    }

   
    @GetMapping
    public List<MedicationModel> getMedications() {
        return medicationService.getAllMedications();
    }

    @GetMapping("/{id}")
    public Object getMedicationByID(@PathVariable int id) {
        Map<String, Object> response = new LinkedHashMap<>();

        try {
            MedicationModel med = medicationService.getMedicationByID(id);
            if (med == null) {
                response.put("message", "ERROR: Medication not found");
                response.put("idRequested", id);
                return response;
            }
            return med;
        } catch (Exception e) {
            response.put("message", "ERROR searching for medication");
            response.put("reason", e.getMessage());
            return response;
        }
    }

    @PostMapping
    public Map<String, Object> addNewMedication(@RequestBody MedicationModel newMed) {
        Map<String, Object> response = new LinkedHashMap<>();

        try {
            medicationService.addMedication(newMed);
            response.put("message", "Medication added successfully !!!");
            response.put("medicationName", newMed.getMedicationName());
            return response;
        } catch (Exception e) {
            response.put("message", "ERROR adding medication");
            response.put("Reason", e.getMessage());
            return response;
        }
    }

    @DeleteMapping("/{id}")
    public Map<String, Object> deleteMedication(@PathVariable int id) {
        Map<String, Object> response = new LinkedHashMap<>();

        try {
            MedicationModel med = medicationService.getMedicationByID(id);

            if (med == null) {
                response.put("message", "ERROR: Medication not found");
                response.put("idRequested", id);
                return response;
            }
            boolean isDeleted = medicationService.deleteMedicationByID(id);

            if (isDeleted == false) {
                response.put("message", "ERROR: Medication was'nt Deleted");
                response.put("idRequested", id);
                return response;
            }
            response.put("message", "Medication Deleted Successfully !!!!");
            response.put("idRequested", id);
            response.put("deleted medication name", med.getMedicationName());
            return response;
        } catch (Exception e) {
            response.put("message", "ERROR DELETING medication");
            if (e.getMessage() != null && e.getMessage().toLowerCase().contains("foreign key")) {
                response.put("reason", "This medication is linked to active prescriptions and cannot be deleted");
            } else {
                response.put("reason", e.getMessage());
            }
            return response;
        }
    }

    @PutMapping("/{id}")
    public Map<String, Object> updateMedication(@PathVariable int id, @RequestBody MedicationModel med) {
        Map<String, Object> response = new LinkedHashMap<>();

        try {
            MedicationModel existingMed = medicationService.getMedicationByID(id);

            if (existingMed == null) {
                response.put("message", "ERROR: Medication not found");
                response.put("idRequested", id);
                return response;
            }

            medicationService.updateMedicationById(id, med);

            response.put("message", "Medication Updated Successfully !!!");
            response.put("medicationName", med.getMedicationName());
            response.put("created date", existingMed.getCreatedAt());

            return response;
        } catch (Exception e) {
            response.put("message", "ERROR Updating medication");
            response.put("Reason", e.getMessage());
            return response;
        }
    }

    @PatchMapping("/{id}")
    public Map<String, Object> partialMedicationUpdate(@PathVariable int id, @RequestBody Map<String, Object> toUpdate) {
        Map<String, Object> response = new LinkedHashMap<>();

        try {
            MedicationModel existingMed = medicationService.getMedicationByID(id);

            if (existingMed == null) {
                response.put("message", "ERROR: Medication not found");
                response.put("idRequested", id);
                return response;
            }
            if (toUpdate.containsKey("medicationName")) {
                existingMed.setMedicationName((String) toUpdate.get("medicationName"));
            }

            medicationService.updateMedicationById(id, existingMed);

            response.put("message", "Medication Updated Successfully !!!");
            response.put("medicationName", existingMed.getMedicationName());
            response.put("created date", existingMed.getCreatedAt());

            return response;
        } catch (Exception e) {
            response.put("message", "ERROR Updating medication");
            response.put("Reason", e.getMessage());
            return response;
        }
    }
}
