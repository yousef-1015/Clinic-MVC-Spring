package com.example.clinicmvcspring.controllers;

import java.util.Map;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.clinicmvcspring.dtos.ErrorResponseDTO;
import com.example.clinicmvcspring.dtos.PaginatedListDTO;
import com.example.clinicmvcspring.models.Medication;
import com.example.clinicmvcspring.services.MedicationService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/medications")
@Tag(name = "Medication Management", description = "Make All CRUD Operations On Medications")
public class MedicationController {
    private final MedicationService medicationService;

    public MedicationController(MedicationService medicationService) {
        this.medicationService = medicationService;
    }

    @GetMapping("")
    @Operation(summary = "Get All Medications", description = "Retrieve All Medications From The Database, [Requires Role: ADMIN]")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success", content = @Content(schema = @Schema(implementation = PaginatedListDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid parameters: page must be >= 0 and size must be between 1 and 50", content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized, Missing or invalid JWT token", content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))),
            @ApiResponse(responseCode = "403", description = "Forbidden, Requires ADMIN role", content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class)))
    })
    public ResponseEntity<?> getMedications(
            @Parameter(description = "Page index starting from zero", example = "0") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Number of medications per page (1-50)", example = "5") @RequestParam(defaultValue = "5") int size) {

        if (page < 0) {
            ErrorResponseDTO error = new ErrorResponseDTO("Page Number Must Be Positive", 400);
            return ResponseEntity.status(400).body(error);
        }
        if (size <= 0 || size > 50) {
            ErrorResponseDTO error = new ErrorResponseDTO("Size must be between 1 and 50", 400);
            return ResponseEntity.status(400).body(error);
        }

        Pageable pageable = PageRequest.of(page, size);

        Page<Medication> medPage = medicationService.getAllMedications(pageable);
        long total = medPage.getTotalElements();
        PaginatedListDTO<Medication> response = new PaginatedListDTO<>(medPage.getContent(), page, size, total);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get A Single Medication", description = "Retrieve a Single Medication By ID, [Requires Role: ADMIN]")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success", content = @Content(schema = @Schema(implementation = Medication.class))),
            @ApiResponse(responseCode = "400", description = "ID must be a positive number", content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized, Missing or invalid JWT token", content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))),
            @ApiResponse(responseCode = "403", description = "Forbidden, Requires ADMIN role", content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "No medication found with that ID", content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class)))
    })
    public ResponseEntity<?> getMedicationByID(
            @Parameter(description = "Database ID of the medication to retrieve", example = "1") @PathVariable int id) {

        if (id <= 0) {
            return ResponseEntity.status(400)
                    .body(new ErrorResponseDTO("ID must be a positive number", 400));
        }
        Optional<Medication> med = medicationService.getMedicationByID(id);
        if (med.isEmpty()) {
            return ResponseEntity.status(404)
                    .body(new ErrorResponseDTO("No medication found with id: " + id, 404));
        }
        return ResponseEntity.ok(med.get());
    }

    @PostMapping
    @Operation(summary = "Add A New Medication", description = "Insert A New Medication Into The Database, [Requires Role: ADMIN]")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Medication Created Successfully", content = @Content(schema = @Schema(implementation = Medication.class))),
            @ApiResponse(responseCode = "409", description = "Medication Name Already Exists", content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized, Missing or invalid JWT token", content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))),
            @ApiResponse(responseCode = "403", description = "Forbidden, Requires ADMIN role", content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request body", content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class)))
    })
    public ResponseEntity<?> addNewMedication(@Valid @RequestBody Medication newMed) {

        return ResponseEntity.status(201).body(medicationService.addMedication(newMed));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete A Medication", description = "Delete a Medication From The Database Based on Id, [Requires Role: ADMIN]")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Medication Deleted Successfully", content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized, Missing or invalid JWT token", content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))),
            @ApiResponse(responseCode = "403", description = "Forbidden, Requires ADMIN role", content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "No medication found with that ID", content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "ID must be a positive number", content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class)))
    })
    public ResponseEntity<?> deleteMedication(
            @Parameter(description = "ID of the medication to delete", example = "1") @PathVariable int id) {

        if (id <= 0) {
            return ResponseEntity.status(400)
                    .body(new ErrorResponseDTO("ID must be a positive number", 400));
        }
        Optional<Medication> med = medicationService.getMedicationByID(id);
        if (med.isEmpty()) {
            return ResponseEntity.status(404)
                    .body(new ErrorResponseDTO("No Medication found with id: " + id, 404));
        }
        medicationService.deleteMedicationByID(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update Medication Details", description = "Update full information of an existing medication by ID, [Requires Role: ADMIN]")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Medication Updated Successfully", content = @Content(schema = @Schema(implementation = Medication.class))),
            @ApiResponse(responseCode = "400", description = "Invalid ID or request body validation failed", content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized, Missing or invalid JWT token", content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))),
            @ApiResponse(responseCode = "403", description = "Forbidden, Requires ADMIN role", content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "No medication found with that ID", content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class)))
    })
    public ResponseEntity<?> updateMedication(
            @Parameter(description = "ID of the medication to update", example = "1") @PathVariable int id,
            @Valid @RequestBody Medication med) {

        if (id <= 0) {
            return ResponseEntity.status(400)
                    .body(new ErrorResponseDTO("ID must be a positive number", 400));
        }
        Optional<Medication> existingMed = medicationService.getMedicationByID(id);
        if (existingMed.isEmpty()) {
            return ResponseEntity.status(404)
                    .body(new ErrorResponseDTO("No Medication found with id: " + id, 404));
        }
        med.setId(id);
        med.setCreatedAt(existingMed.get().getCreatedAt());
        medicationService.updateMedicationById(id, med);
        return ResponseEntity.ok(med);
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Partially Update A Medication", description = "Partially update specific fields (like medicationName) of an existing medication by ID, [Requires Role: ADMIN]")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Medication Partially Updated Successfully", content = @Content(schema = @Schema(implementation = Medication.class))),
            @ApiResponse(responseCode = "400", description = "ID must be a positive number", content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized, Missing or invalid JWT token", content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))),
            @ApiResponse(responseCode = "403", description = "Forbidden, Requires ADMIN role", content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "No medication found with that ID", content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class)))
    })
    public ResponseEntity<?> partialMedicationUpdate(
            @Parameter(description = "ID of the medication to partially update", example = "1") @PathVariable int id,
            @RequestBody Map<String, Object> toUpdate) {

        if (id <= 0) {
            return ResponseEntity.status(400)
                    .body(new ErrorResponseDTO("ID must be a positive number", 400));
        }
        Optional<Medication> existingMed = medicationService.getMedicationByID(id);
        if (existingMed.isEmpty()) {
            return ResponseEntity.status(404)
                    .body(new ErrorResponseDTO("No Medication found with id: " + id, 404));
        }
        if (toUpdate.containsKey("medicationName")) {
            existingMed.get().setMedicationName((String) toUpdate.get("medicationName"));
        }
        medicationService.updateMedicationById(id, existingMed.get());
        return ResponseEntity.ok(existingMed.get());
    }
}
