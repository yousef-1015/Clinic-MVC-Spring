package com.example.clinicmvcspring.controllers;

import org.springframework.web.bind.annotation.RestController;

import com.example.clinicmvcspring.dtos.ErrorResponseDTO;
import com.example.clinicmvcspring.models.DoctorModel;
import com.example.clinicmvcspring.services.DoctorService;

import java.util.List;
import java.util.Map;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
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
    // ? not MODEL because we might return error Dto
    public ResponseEntity<?> getDoctorByID(@PathVariable int id) {

        try {
            DoctorModel doc = doctorService.getDoctorByID(id);
            if (doc == null) {
                ErrorResponseDTO error = new ErrorResponseDTO("No doctor found with id: " + id, 404);
                return ResponseEntity.status(404).body(error);
            }

            return ResponseEntity.ok(doc);
        } catch (Exception e) {
            ErrorResponseDTO error = new ErrorResponseDTO(
                    "Error Finding doctor: " + e.getMessage(), 500);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }

    }

    @PostMapping
    public ResponseEntity<?> addNewDoctor(@RequestBody DoctorModel newDoc) {

        try {
            doctorService.addDoctor(newDoc);
            return ResponseEntity.status(201).body(newDoc);
        } catch (DuplicateKeyException e) {
            ErrorResponseDTO error = new ErrorResponseDTO("Email already used: " + newDoc.getEmail(), 409);
            return ResponseEntity.status(409).body(error);
        } catch (Exception e) {
            ErrorResponseDTO error = new ErrorResponseDTO(
                    "Error adding doctor: " + e.getMessage(), 500);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteDoctor(@PathVariable int id) {

        try {
            DoctorModel doc = doctorService.getDoctorByID(id);
            if (doc == null) {
                ErrorResponseDTO error = new ErrorResponseDTO("No Doctor found with id: " + id, 404);
                return ResponseEntity.status(404).body(error); // 404
            }

            doctorService.deleteDoctorByID(id);
            return ResponseEntity.noContent().build(); // 204
        } catch (DataIntegrityViolationException e) { // Violating DB constraint
            // Doctor has appointments
            ErrorResponseDTO error = new ErrorResponseDTO("Cannot delete: doctor has appointments", 409);
            return ResponseEntity.status(HttpStatus.CONFLICT).body(error); // 409
        } catch (Exception e) {
            ErrorResponseDTO error = new ErrorResponseDTO(
                    "Error deleting doctor: " + e.getMessage(), 500);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }

    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateDoctor(@PathVariable int id, @RequestBody DoctorModel doc) {

        try {

            DoctorModel existingDoc = doctorService.getDoctorByID(id);
            if (existingDoc == null) {
                ErrorResponseDTO error = new ErrorResponseDTO("No Doctor found with id: " + id, 404);
                return ResponseEntity.status(404).body(error); // 404
            }

            doc.setId(id);
            doc.setHireDate(existingDoc.getHireDate());
            doctorService.updateDoctorById(id, doc);
            return ResponseEntity.ok(doc); // 200

        } catch (DuplicateKeyException e) {
            ErrorResponseDTO error = new ErrorResponseDTO("Email already used: " + doc.getEmail(), 409);
            return ResponseEntity.status(409).body(error);
        } catch (Exception e) {
            ErrorResponseDTO error = new ErrorResponseDTO(
                    "Error Updating doctor: " + e.getMessage(), 500);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }

    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> partialDocUpdate(@PathVariable int id, @RequestBody Map<String, Object> toUpdate) {

        try {

            DoctorModel existingDoc = doctorService.getDoctorByID(id);
            if (existingDoc == null) {
                ErrorResponseDTO error = new ErrorResponseDTO("No Doctor found with id: " + id, 404);
                return ResponseEntity.status(404).body(error); // 404
            }

            if (toUpdate.containsKey("firstName")) {
                existingDoc.setFirstName((String) toUpdate.get("firstName"));
            }
            if (toUpdate.containsKey("lastName")) {
                existingDoc.setLastName((String) toUpdate.get("lastName"));
            }
            if (toUpdate.containsKey("email")) {
                existingDoc.setEmail((String) toUpdate.get("email"));
            }
            if (toUpdate.containsKey("specialty")) {
                existingDoc.setSpecialty((String) toUpdate.get("specialty"));
            }
            if (toUpdate.containsKey("salary")) {
                Number salaryVal = (Number) toUpdate.get("salary");
                existingDoc.setSalary(salaryVal.doubleValue());
            }

            doctorService.updateDoctorById(id, existingDoc);
            return ResponseEntity.ok(existingDoc); // 200

        
        } catch (DuplicateKeyException e) {
            ErrorResponseDTO error = new ErrorResponseDTO("Email already used: ", 409);
            return ResponseEntity.status(409).body(error);
        } catch (Exception e) {
            ErrorResponseDTO error = new ErrorResponseDTO(
                    "Error Updating doctor: " + e.getMessage(), 500);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }

    }

}
