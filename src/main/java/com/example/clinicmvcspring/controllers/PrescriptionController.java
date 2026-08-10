package com.example.clinicmvcspring.controllers;

import java.util.Map;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
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
import com.example.clinicmvcspring.dtos.PrescriptionDetailDTO;
import com.example.clinicmvcspring.models.Appointment;
import com.example.clinicmvcspring.models.Prescription;
import com.example.clinicmvcspring.services.PrescriptionService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;

@RestController
@RequestMapping("/api/v1/prescriptions")
@Tag(name = "Prescription Management", description = "Make All CRUD Operations On Prescriptions")
@Validated
public class PrescriptionController {
    private final PrescriptionService prescriptionService;

    public PrescriptionController(PrescriptionService prescriptionService) {
        this.prescriptionService = prescriptionService;
    }

    @GetMapping
    @Operation(summary = "Get All Prescriptions", description = "Retrieve All Prescriptions From The Database, [Requires Role: ADMIN]")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success", content = @Content(schema = @Schema(implementation = PaginatedListDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid parameters: page must be >= 0 and size must be between 1 and 50", content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized, Missing or invalid JWT token", content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))),
            @ApiResponse(responseCode = "403", description = "Forbidden, Requires ADMIN role", content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class)))
    })
    public ResponseEntity<?> getPrescriptions(
            @Parameter(description = "Page index starting from zero", example = "0") @RequestParam(defaultValue = "0") @Min(value = 0, message = "Page Number Must Be Positive") int page,
            @Parameter(description = "Number of prescriptions per page (1-50)", example = "5") @RequestParam(defaultValue = "5") @Min(value = 1, message = "Size must be between 1 and 50") @Max(value = 50, message = "Size must be between 1 and 50") int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<PrescriptionDetailDTO> presPage = prescriptionService.getAllPrescriptionsPaginated(pageable);
        long total = presPage.getTotalElements();
        PaginatedListDTO<PrescriptionDetailDTO> response = new PaginatedListDTO<>(presPage.getContent(), page, size,
                total);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get A Single Prescription", description = "Retrieve a Single Prescription By ID, [Requires Role: ADMIN]")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success", content = @Content(schema = @Schema(implementation = PrescriptionDetailDTO.class))),
            @ApiResponse(responseCode = "400", description = "ID must be a positive number", content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized, Missing or invalid JWT token", content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))),
            @ApiResponse(responseCode = "403", description = "Forbidden, Requires ADMIN role", content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "No prescription found with that ID", content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class)))
    })
    public ResponseEntity<?> getPrescriptionByID(
            @Parameter(description = "Database ID of the prescription to retrieve", example = "1") @PathVariable @Positive(message = "ID must be a positive number") int id) {

        // Fetch the detailed DTO directly
        Optional<PrescriptionDetailDTO> details = prescriptionService.getPrescriptionDetailsByID(id);

        if (details.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponseDTO("Prescription not found with ID: " + id, 404));
        }

        return ResponseEntity.ok(details.get());
    }

    @PostMapping
    @Operation(summary = "Add A New Prescription", description = "Insert A New Prescription With Medications Into The Database, [Requires Role: ADMIN]")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Prescription Created Successfully", content = @Content(schema = @Schema(implementation = PrescriptionDetailDTO.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized, Missing or invalid JWT token", content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))),
            @ApiResponse(responseCode = "403", description = "Forbidden, Requires ADMIN role", content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request body", content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class)))
    })
    public ResponseEntity<?> addNewPrescription(@Valid @RequestBody PrescriptionDetailDTO newPresDto) {

        return ResponseEntity.status(201).body(prescriptionService.addPrescriptionWithMedications(newPresDto));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete A Prescription", description = "Delete a Prescription From The Database Based on Id, [Requires Role: ADMIN]")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Prescription Deleted Successfully", content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized, Missing or invalid JWT token", content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))),
            @ApiResponse(responseCode = "403", description = "Forbidden, Requires ADMIN role", content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "No prescription found with that ID", content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "ID must be a positive number", content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class)))
    })
    public ResponseEntity<?> deletePrescription(
            @Parameter(description = "ID of the prescription to delete", example = "1") @PathVariable @Positive(message = "ID must be a positive number") int id) {

        Optional<Prescription> pres = prescriptionService.getPrescriptionByID(id);

        if (pres.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponseDTO("Prescription not found with ID: " + id, 404));
        }
        prescriptionService.deletePrescriptionByID(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update Prescription Details", description = "Update full information of an existing prescription by ID, [Requires Role: ADMIN]")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Prescription Updated Successfully", content = @Content(schema = @Schema(implementation = PrescriptionDetailDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid ID or request body validation failed", content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized, Missing or invalid JWT token", content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))),
            @ApiResponse(responseCode = "403", description = "Forbidden, Requires ADMIN role", content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "No prescription found with that ID", content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class)))
    })
    public ResponseEntity<?> updatePrescription(
            @Parameter(description = "ID of the prescription to update", example = "1") @PathVariable @Positive(message = "ID must be a positive number") int id,
            @Valid @RequestBody Prescription pres) {

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
    @Operation(summary = "Partially Update A Prescription", description = "Partially update specific fields (like prescriptionNotes, appointmentId) of an existing prescription by ID, [Requires Role: ADMIN]")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Prescription Partially Updated Successfully", content = @Content(schema = @Schema(implementation = PrescriptionDetailDTO.class))),
            @ApiResponse(responseCode = "400", description = "ID must be a positive number", content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized, Missing or invalid JWT token", content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))),
            @ApiResponse(responseCode = "403", description = "Forbidden, Requires ADMIN role", content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "No prescription found with that ID", content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class)))
    })
    public ResponseEntity<?> partialPrescriptionUpdate(
            @Parameter(description = "ID of the prescription to partially update", example = "1") @PathVariable @Positive(message = "ID must be a positive number") int id,
            @RequestBody Map<String, Object> toUpdate) {

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
