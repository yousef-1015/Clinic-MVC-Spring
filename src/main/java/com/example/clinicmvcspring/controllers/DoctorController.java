package com.example.clinicmvcspring.controllers;

import org.springframework.web.bind.annotation.RestController;

import com.example.clinicmvcspring.dtos.ErrorResponseDTO;
import com.example.clinicmvcspring.models.DoctorModel;
import com.example.clinicmvcspring.services.DoctorService;

import jakarta.validation.Valid;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
@RequestMapping("/api/v1/doctors")
public class DoctorController {
    private final DoctorService doctorService;

    public DoctorController(DoctorService doctorService) {
        this.doctorService = doctorService;
    }

    // Get all doctors as JSON
    @GetMapping
    public ResponseEntity<List<DoctorModel>> getDoctors() {
        List<DoctorModel> allDocs = doctorService.getAllDoctors();
        return ResponseEntity.ok(allDocs);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getDoctorByID(@PathVariable int id) {
        if (id <= 0) {
            return ResponseEntity.status(400)
                    .body(new ErrorResponseDTO("ID must be a positive number", 400));
        }
        DoctorModel doc = doctorService.getDoctorByID(id);
        if (doc == null) {// Empty from repo exception
            return ResponseEntity.status(404)
                    .body(new ErrorResponseDTO("No doctor found with id: " + id, 404));
        }
        return ResponseEntity.ok(doc);
    }

    @PostMapping
    public ResponseEntity<?> addNewDoctor(@Valid @RequestBody DoctorModel newDoc) {
        doctorService.addDoctor(newDoc);
        return ResponseEntity.status(201).body(newDoc); // 201

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteDoctor(@PathVariable int id) {
        if (id <= 0) {
            return ResponseEntity.status(400)
                    .body(new ErrorResponseDTO("ID must be a positive number", 400));
        }
        DoctorModel doc = doctorService.getDoctorByID(id);
        if (doc == null) {
            return ResponseEntity.status(404)
                    .body(new ErrorResponseDTO("No Doctor found with id: " + id, 404));
        }
        doctorService.deleteDoctorByID(id);
        return ResponseEntity.noContent().build(); // 204

    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateDoctor(@PathVariable int id, @Valid @RequestBody DoctorModel doc) {
        if (id <= 0) {
            return ResponseEntity.status(400)
                    .body(new ErrorResponseDTO("ID must be a positive number", 400));
        }
        DoctorModel existingDoc = doctorService.getDoctorByID(id);
        if (existingDoc == null) {
            return ResponseEntity.status(404)
                    .body(new ErrorResponseDTO("No Doctor found with id: " + id, 404));
        }
        doc.setId(id);
        doc.setHireDate(existingDoc.getHireDate());
        doctorService.updateDoctorById(id, doc);
        return ResponseEntity.ok(doc); // 200
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> partialDocUpdate(@PathVariable int id, @RequestBody Map<String, Object> toUpdate) {
        if (id <= 0) {
            return ResponseEntity.status(400)
                    .body(new ErrorResponseDTO("ID must be a positive number", 400));
        }
        DoctorModel existingDoc = doctorService.getDoctorByID(id);
        if (existingDoc == null) {
            return ResponseEntity.status(404)
                    .body(new ErrorResponseDTO("No Doctor found with id: " + id, 404));
        }
        if (toUpdate.containsKey("firstName"))
            existingDoc.setFirstName((String) toUpdate.get("firstName"));
        if (toUpdate.containsKey("lastName"))
            existingDoc.setLastName((String) toUpdate.get("lastName"));
        if (toUpdate.containsKey("email"))
            existingDoc.setEmail((String) toUpdate.get("email"));
        if (toUpdate.containsKey("specialty"))
            existingDoc.setSpecialty((String) toUpdate.get("specialty"));
        if (toUpdate.containsKey("salary"))
            existingDoc.setSalary(((Number) toUpdate.get("salary")).doubleValue());
        doctorService.updateDoctorById(id, existingDoc);
        return ResponseEntity.ok(existingDoc); // 200
        // DuplicateKeyException → GlobalExceptionHandler ✅
    }

}
