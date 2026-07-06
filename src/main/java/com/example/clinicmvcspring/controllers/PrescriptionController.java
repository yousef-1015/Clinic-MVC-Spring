package com.example.clinicmvcspring.controllers;

import org.springframework.web.bind.annotation.RestController;

import com.example.clinicmvcspring.models.Appointment;
import com.example.clinicmvcspring.models.Prescription;
import com.example.clinicmvcspring.services.PrescriptionService;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;
import com.example.clinicmvcspring.dtos.ErrorResponseDTO;
import com.example.clinicmvcspring.dtos.PaginatedListDTO;
import com.example.clinicmvcspring.dtos.PrescriptionDetailDTO;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
@RequestMapping("/api/v1/prescriptions")
public class PrescriptionController {
    private final PrescriptionService prescriptionService;

    public PrescriptionController(PrescriptionService prescriptionService) {
        this.prescriptionService = prescriptionService;
    }

    @GetMapping
    public ResponseEntity<?> getPrescriptions(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {
        if (page < 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponseDTO("Page number cannot be negative", 400));
        }
        if (size <= 0 || size > 50) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponseDTO("Page size must be between 1 and 50", 400));
        }
        List<PrescriptionDetailDTO> prescriptions = prescriptionService.getAllPrescriptionsPaginated(page, size);
        long total = prescriptionService.count();
        PaginatedListDTO<PrescriptionDetailDTO> response = new PaginatedListDTO<>(prescriptions, page, size, total);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getPrescriptionByID(@PathVariable int id) {

        if (id <= 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponseDTO("ID must be greater than 0", 400));
        }

        // Fetch the detailed DTO directly
        Optional<PrescriptionDetailDTO> details = prescriptionService.getPrescriptionDetailsByID(id);

        if (details.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponseDTO("Prescription not found with ID: " + id, 404));
        }

        return ResponseEntity.ok(details.get());
    }

    @PostMapping
    public ResponseEntity<?> addNewPrescription(@Valid @RequestBody PrescriptionDetailDTO newPresDto) {
        return ResponseEntity.status(201).body(prescriptionService.addPrescriptionWithMedications(newPresDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePrescription(@PathVariable int id) {

        if (id <= 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponseDTO("ID must be greater than 0", 400));
        }
        Optional<Prescription> pres = prescriptionService.getPrescriptionByID(id);

        if (pres.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponseDTO("Prescription not found with ID: " + id, 404));
        }
        prescriptionService.deletePrescriptionByID(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updatePrescription(@PathVariable int id, @Valid @RequestBody Prescription pres) {
        if (id <= 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponseDTO("ID must be greater than 0", 400));
        }
        Optional<Prescription> existingPres = prescriptionService.getPrescriptionByID(id);

        if (existingPres.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponseDTO("Prescription not found with ID: " + id, 404));
        }
        pres.setId(id);
        pres.setCreatedAt(existingPres.get().getCreatedAt());

        return ResponseEntity.ok(prescriptionService.updatePrescriptionById(id, pres));

    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> partialPrescriptionUpdate(@PathVariable int id,
            @RequestBody Map<String, Object> toUpdate) {
        if (id <= 0) {
            return ResponseEntity.status(400)
                    .body(new ErrorResponseDTO("ID must be a positive number", 400));
        }
        Optional<Prescription> existingPres = prescriptionService.getPrescriptionByID(id);

        if (existingPres.isEmpty()) {
            return ResponseEntity.status(404)
                    .body(new ErrorResponseDTO("No Prescription found with id: " + id, 404));
        }
        if (toUpdate.containsKey("prescriptionNotes")) {
            existingPres.get().setPrescriptionNotes((String) toUpdate.get("prescriptionNotes"));
        }
        if (toUpdate.containsKey("appointmentId")) {
            Appointment appo = new Appointment();
            appo.setId(((Number) toUpdate.get("appointmentId")).intValue());
            existingPres.get().setAppointment(appo);
        }

        

        return ResponseEntity.ok(prescriptionService.updatePrescriptionById(id, existingPres.get())); // 200

    }

}
