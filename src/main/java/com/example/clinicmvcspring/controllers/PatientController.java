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
import com.example.clinicmvcspring.models.Patient;
import com.example.clinicmvcspring.services.PatientService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/patients")
@Tag(name = "Patient Management", description = "Make All CRUD Operations On Patients")
public class PatientController {

    private final PatientService patientService;

    public PatientController(PatientService patientService) {
        this.patientService = patientService;
    }

    @GetMapping("")
    @Operation(summary = "Get All Patients", description = "Retrieve All Patients From The Database, [Requires Role: ADMIN]")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success", content = @Content(schema = @Schema(implementation = PaginatedListDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid parameters: page must be >= 0 and size must be between 1 and 50", content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized, Missing or invalid JWT token", content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))),
            @ApiResponse(responseCode = "403", description = "Forbidden, Requires ADMIN role", content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class)))
    })
    public ResponseEntity<?> getAllPatients(
            @Parameter(description = "Page index starting from zero", example = "0") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Number of patients per page (1-50)", example = "5") @RequestParam(defaultValue = "5") int size) {

        if (page < 0) {
            ErrorResponseDTO error = new ErrorResponseDTO("Page Number Must Be Positive", 400);
            return ResponseEntity.status(400).body(error);
        }
        if (size <= 0 || size > 50) {
            ErrorResponseDTO error = new ErrorResponseDTO("Size must be between 1 and 50", 400);
            return ResponseEntity.status(400).body(error);
        }

        Pageable pageable = PageRequest.of(page, size);

        Page<Patient> PatientPage = patientService.getAllPatients(pageable);
        long total = PatientPage.getTotalElements();
        PaginatedListDTO<Patient> response = new PaginatedListDTO<>(PatientPage.getContent(), page, size, total);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get A Single Patient", description = "Retrieve a Single Patient By The Patient ID, [Requires Role: ADMIN]")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success", content = @Content(schema = @Schema(implementation = Patient.class))),
            @ApiResponse(responseCode = "400", description = "ID must be a positive number", content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized, Missing or invalid JWT token", content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))),
            @ApiResponse(responseCode = "403", description = "Forbidden, Requires ADMIN role", content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "No patient found with that ID", content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class)))
    })
    public ResponseEntity<?> getPatientByID(
            @Parameter(description = "Database ID of the patient to retrieve", example = "1") @PathVariable int id) {

        if (id <= 0) {
            return ResponseEntity.status(400)
                    .body(new ErrorResponseDTO("ID must be a positive number", 400));
        }
        Optional<Patient> pat = patientService.getPatientByID(id);
        if (pat.isEmpty()) {
            return ResponseEntity.status(404)
                    .body(new ErrorResponseDTO("No patient found with id: " + id, 404));
        }
        return ResponseEntity.ok(pat.get());
    }

    @PostMapping
    @Operation(summary = "Add A New Patient", description = "Insert A New Patient Into The Database, [Requires Role: ADMIN]")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Patient Created Successfully", content = @Content(schema = @Schema(implementation = Patient.class))),
            @ApiResponse(responseCode = "409", description = "Email Already Used", content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized, Missing or invalid JWT token", content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))),
            @ApiResponse(responseCode = "403", description = "Forbidden, Requires ADMIN role", content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request body", content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class)))
    })
    public ResponseEntity<?> addNewPatient(@Valid @RequestBody Patient newPatient) {

        return ResponseEntity.status(201).body(patientService.addPatient(newPatient));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete A Patient", description = "Delete a Patient From The Database Based on Id, [Requires Role: ADMIN]")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Patient Deleted Successfully", content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized, Missing or invalid JWT token", content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))),
            @ApiResponse(responseCode = "403", description = "Forbidden, Requires ADMIN role", content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "No patient found with that ID", content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "ID must be a positive number", content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class)))
    })
    public ResponseEntity<?> deletePatient(
            @Parameter(description = "ID of the patient to delete", example = "1") @PathVariable int id) {

        if (id <= 0) {
            return ResponseEntity.status(400)
                    .body(new ErrorResponseDTO("ID must be a positive number", 400));
        }
        Optional<Patient> pat = patientService.getPatientByID(id);
        if (pat.isEmpty()) {
            return ResponseEntity.status(404)
                    .body(new ErrorResponseDTO("No Patient found with id: " + id, 404));
        }
        patientService.deletePatientByID(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update Patient Details", description = "Update full information of an existing patient by ID, [Requires Role: ADMIN]")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Patient Updated Successfully", content = @Content(schema = @Schema(implementation = Patient.class))),
            @ApiResponse(responseCode = "400", description = "Invalid ID or request body validation failed", content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized, Missing or invalid JWT token", content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))),
            @ApiResponse(responseCode = "403", description = "Forbidden, Requires ADMIN role", content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "No patient found with that ID", content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class)))
    })
    public ResponseEntity<?> updatePatient(
            @Parameter(description = "ID of the patient to update", example = "1") @PathVariable int id,
            @Valid @RequestBody Patient pat) {

        if (id <= 0) {
            return ResponseEntity.status(400)
                    .body(new ErrorResponseDTO("ID must be a positive number", 400));
        }
        Optional<Patient> existingPat = patientService.getPatientByID(id);
        if (existingPat.isEmpty()) {
            return ResponseEntity.status(404)
                    .body(new ErrorResponseDTO("No Patient found with id: " + id, 404));
        }
        pat.setId(id);
        pat.setCreatedAt(existingPat.get().getCreatedAt());
        patientService.updatePatientById(id, pat);
        return ResponseEntity.ok(pat);
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Partially Update A Patient", description = "Partially update specific fields (like firstName, lastName, or email) of an existing patient by ID, [Requires Role: ADMIN]")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Patient Partially Updated Successfully", content = @Content(schema = @Schema(implementation = Patient.class))),
            @ApiResponse(responseCode = "400", description = "ID must be a positive number", content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized, Missing or invalid JWT token", content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))),
            @ApiResponse(responseCode = "403", description = "Forbidden, Requires ADMIN role", content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "No patient found with that ID", content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class)))
    })
    public ResponseEntity<?> partialPatientUpdate(
            @Parameter(description = "ID of the patient to partially update", example = "1") @PathVariable int id,
            @RequestBody Map<String, Object> toUpdate) {

        if (id <= 0) {
            return ResponseEntity.status(400)
                    .body(new ErrorResponseDTO("ID must be a positive number", 400));
        }
        Optional<Patient> existingPat = patientService.getPatientByID(id);
        if (existingPat.isEmpty()) {
            return ResponseEntity.status(404)
                    .body(new ErrorResponseDTO("No Patient found with id: " + id, 404));
        }
        if (toUpdate.containsKey("firstName")) {
            existingPat.get().setFirstName((String) toUpdate.get("firstName"));
        }
        if (toUpdate.containsKey("lastName")) {
            existingPat.get().setLastName((String) toUpdate.get("lastName"));
        }
        if (toUpdate.containsKey("email")) {
            existingPat.get().setEmail((String) toUpdate.get("email"));
        }
        patientService.updatePatientById(id, existingPat.get());
        return ResponseEntity.ok(existingPat.get());
    }

}
