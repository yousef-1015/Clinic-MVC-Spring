package com.example.clinicmvcspring.controllers;

import org.springframework.web.bind.annotation.RestController;

import com.example.clinicmvcspring.dtos.ErrorResponseDTO;
import com.example.clinicmvcspring.dtos.PaginatedListDto;
import com.example.clinicmvcspring.models.Medication;
import com.example.clinicmvcspring.services.MedicationService;

import jakarta.validation.Valid;


import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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

    @GetMapping("")
    public ResponseEntity<?> getMedications(@RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {

        if (page < 0) {
            ErrorResponseDTO error = new ErrorResponseDTO("Page Number Must Be Positive", 400);
            return ResponseEntity.status(400).body(error);
        }
        if (size <= 0 || size > 50) {
            ErrorResponseDTO error = new ErrorResponseDTO("Size must be between 1 and 50", 400);
            return ResponseEntity.status(400).body(error);
        }

        List<Medication> allMeds = medicationService.getAllMedications(page, size);
        int total = medicationService.countMedications();
        PaginatedListDto<Medication> response = new PaginatedListDto<>(allMeds, page, size, total);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getMedicationByID(@PathVariable int id) {
        if (id <= 0) {
            return ResponseEntity.status(400)
                    .body(new ErrorResponseDTO("ID must be a positive number", 400));
        }
        Medication med = medicationService.getMedicationByID(id);
        if (med == null) {
            return ResponseEntity.status(404)
                    .body(new ErrorResponseDTO("No medication found with id: " + id, 404));
        }
        return ResponseEntity.ok(med);
    }

    @PostMapping
    public ResponseEntity<?> addNewMedication(@Valid @RequestBody Medication newMed) {
        int newID = medicationService.addMedication(newMed);
        newMed.setId(newID);
        return ResponseEntity.status(201).body(newMed);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteMedication(@PathVariable int id) {
        if (id <= 0) {
            return ResponseEntity.status(400)
                    .body(new ErrorResponseDTO("ID must be a positive number", 400));
        }
        Medication med = medicationService.getMedicationByID(id);
        if (med == null) {
            return ResponseEntity.status(404)
                    .body(new ErrorResponseDTO("No Medication found with id: " + id, 404));
        }
        medicationService.deleteMedicationByID(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateMedication(@PathVariable int id, @Valid @RequestBody Medication med) {
        if (id <= 0) {
            return ResponseEntity.status(400)
                    .body(new ErrorResponseDTO("ID must be a positive number", 400));
        }
        Medication existingMed = medicationService.getMedicationByID(id);
        if (existingMed == null) {
            return ResponseEntity.status(404)
                    .body(new ErrorResponseDTO("No Medication found with id: " + id, 404));
        }
        med.setId(id);
        med.setCreatedAt(existingMed.getCreatedAt());
        medicationService.updateMedicationById(id, med);
        return ResponseEntity.ok(med);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> partialMedicationUpdate(@PathVariable int id, @RequestBody Map<String, Object> toUpdate) {
        if (id <= 0) {
            return ResponseEntity.status(400)
                    .body(new ErrorResponseDTO("ID must be a positive number", 400));
        }
        Medication existingMed = medicationService.getMedicationByID(id);
        if (existingMed == null) {
            return ResponseEntity.status(404)
                    .body(new ErrorResponseDTO("No Medication found with id: " + id, 404));
        }
        if (toUpdate.containsKey("medicationName")) {
            existingMed.setMedicationName((String) toUpdate.get("medicationName"));
        }
        medicationService.updateMedicationById(id, existingMed);
        return ResponseEntity.ok(existingMed);
    }
}
