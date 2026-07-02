package com.example.clinicmvcspring.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.clinicmvcspring.dtos.ErrorResponseDTO;
import com.example.clinicmvcspring.dtos.PaginatedListDto;
import com.example.clinicmvcspring.models.Patient;
import com.example.clinicmvcspring.services.PatientService;

import jakarta.validation.Valid;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/api/v1/patients")
public class PatientController {

    private final PatientService patientService;

    public PatientController(PatientService patientService) {
        this.patientService = patientService;
    }

    @GetMapping("")
    public ResponseEntity<?> getAllPatients(@RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {

        if (page < 0) {
            ErrorResponseDTO error = new ErrorResponseDTO("Page Number Must Be Positive", 400);
            return ResponseEntity.status(400).body(error);
        }
        if (size <= 0 || size > 50) {
            ErrorResponseDTO error = new ErrorResponseDTO("Size must be between 1 and 50", 400);
            return ResponseEntity.status(400).body(error);
        }

        List<Patient> allPatients = patientService.getAllPatients(page, size);
        int total = patientService.countPatients();
        PaginatedListDto<Patient> response = new PaginatedListDto<>(allPatients, page, size, total);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getPatientByID(@PathVariable int id) {
        if (id <= 0) {
            return ResponseEntity.status(400)
                    .body(new ErrorResponseDTO("ID must be a positive number", 400));
        }
        Patient pat = patientService.getPatientByID(id);
        if (pat == null) {
            return ResponseEntity.status(404)
                    .body(new ErrorResponseDTO("No patient found with id: " + id, 404));
        }
        return ResponseEntity.ok(pat);
    }

    @PostMapping
    public ResponseEntity<?> addNewPatient(@Valid @RequestBody Patient newPatient) {
        patientService.addPatient(newPatient);
        return ResponseEntity.status(201).body(newPatient);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePatient(@PathVariable int id) {
        if (id <= 0) {
            return ResponseEntity.status(400)
                    .body(new ErrorResponseDTO("ID must be a positive number", 400));
        }
        Patient pat = patientService.getPatientByID(id);
        if (pat == null) {
            return ResponseEntity.status(404)
                    .body(new ErrorResponseDTO("No Patient found with id: " + id, 404));
        }
        patientService.deletePatientByID(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updatePatient(@PathVariable int id, @Valid @RequestBody Patient pat) {
        if (id <= 0) {
            return ResponseEntity.status(400)
                    .body(new ErrorResponseDTO("ID must be a positive number", 400));
        }
        Patient existingPat = patientService.getPatientByID(id);
        if (existingPat == null) {
            return ResponseEntity.status(404)
                    .body(new ErrorResponseDTO("No Patient found with id: " + id, 404));
        }
        pat.setId(id);
        pat.setCreatedAt(existingPat.getCreatedAt());
        patientService.updatePatientById(id, pat);
        return ResponseEntity.ok(pat);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> partialPatientUpdate(@PathVariable int id, @RequestBody Map<String, Object> toUpdate) {
        if (id <= 0) {
            return ResponseEntity.status(400)
                    .body(new ErrorResponseDTO("ID must be a positive number", 400));
        }
        Patient existingPat = patientService.getPatientByID(id);
        if (existingPat == null) {
            return ResponseEntity.status(404)
                    .body(new ErrorResponseDTO("No Patient found with id: " + id, 404));
        }
        if (toUpdate.containsKey("firstName")) {
            existingPat.setFirstName((String) toUpdate.get("firstName"));
        }
        if (toUpdate.containsKey("lastName")) {
            existingPat.setLastName((String) toUpdate.get("lastName"));
        }
        if (toUpdate.containsKey("email")) {
            existingPat.setEmail((String) toUpdate.get("email"));
        }
        patientService.updatePatientById(id, existingPat);
        return ResponseEntity.ok(existingPat);
    }

}
