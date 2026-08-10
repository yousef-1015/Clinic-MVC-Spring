package com.example.clinicmvcspring.controllers;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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

import com.example.clinicmvcspring.dtos.DoctorDTO;
import com.example.clinicmvcspring.dtos.ErrorResponseDTO;
import com.example.clinicmvcspring.dtos.PaginatedListDTO;
import com.example.clinicmvcspring.models.Doctor;
import com.example.clinicmvcspring.services.DoctorService;

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
@RequestMapping("/api/v1/doctors")
@Tag(name = "Doctor Management", description = "Make All CRUD Operations On Doctors")
@Validated
public class DoctorController {
    private final DoctorService doctorService;

    public DoctorController(DoctorService doctorService) {
        this.doctorService = doctorService;
    }

    @GetMapping("")
    @Operation(summary = "Get All Doctors", description = "Retrieve All Doctors From The Database, [Requires Role: ADMIN]")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success", content = @Content(schema = @Schema(implementation = PaginatedListDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid parameters: page must be >= 0 and size must be between 1 and 50", content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized, Missing or invalid JWT token", content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))),
            @ApiResponse(responseCode = "403", description = "Forbidden, Requires ADMIN role", content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class)))

    })
    public ResponseEntity<?> getDoctors(
            @RequestParam(defaultValue = "0") @Parameter(description = "Page index starting from zero", example = "0") @Min(value = 0, message = "Page Number Must Be Positive") int page,
            @Parameter(description = "Number of doctors per page (1-50)", example = "5") @RequestParam(defaultValue = "5") @Min(value = 1, message = "Size must be between 1 and 50") @Max(value = 50, message = "Size must be between 1 and 50") int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<DoctorDTO> doctorPage = doctorService.getAllDoctors(pageable);
        long total = doctorPage.getTotalElements();
        PaginatedListDTO<DoctorDTO> response = new PaginatedListDTO<>(doctorPage.getContent(), page, size, total);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get A Single Doctor", description = "Retrieve a Single Doctors By The Doctors ID, [Requires Role: ADMIN]")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success", content = @Content(schema = @Schema(implementation = Doctor.class))),
            @ApiResponse(responseCode = "400", description = "ID must be a positive number", content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized, Missing or invalid JWT token", content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))),
            @ApiResponse(responseCode = "403", description = "Forbidden, Requires ADMIN role", content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "No doctor found with that ID", content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class)))

    })
    public ResponseEntity<?> getDoctorByID(
            @Parameter(description = "Database ID of the doctor to retrieve", example = "1") @PathVariable @Positive(message = "ID must be a positive number") int id) {
        Optional<Doctor> doc = doctorService.getDoctorByID(id);
        if (doc.isEmpty()) {// Empty from repo exception
            return ResponseEntity.status(404)
                    .body(new ErrorResponseDTO("No doctor found with id: " + id, 404));
        }
        return ResponseEntity.ok(doc.get());
    }

    @PostMapping
    @Operation(summary = "Add A New Doctor", description = "Insert A New Doctor Into The Database, [Requires Role: ADMIN]")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Doctor Created Successfully", content = @Content(schema = @Schema(implementation = Doctor.class))),
            @ApiResponse(responseCode = "409", description = "Email Already Used", content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized, Missing or invalid JWT token", content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))),
            @ApiResponse(responseCode = "403", description = "Forbidden, Requires ADMIN role", content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request body", content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class)))

    })
    public ResponseEntity<?> addNewDoctor(@Valid @RequestBody Doctor newDoc) {
        return ResponseEntity.status(201).body(doctorService.addDoctor(newDoc)); // 201

    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete A Doctor", description = "Delete a Doctor From The Database Based on Id, [Requires Role: ADMIN]")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Doctor Deleted Successfully", content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized, Missing or invalid JWT token", content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))),
            @ApiResponse(responseCode = "403", description = "Forbidden, Requires ADMIN role", content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "No doctor found with that ID", content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "ID must be a positive number", content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class)))

    })
    public ResponseEntity<?> deleteDoctor(
            @Parameter(description = "ID of the doctor to delete", example = "1") @PathVariable @Positive(message = "ID must be a positive number") int id) {
        Optional<Doctor> doc = doctorService.getDoctorByID(id);
        if (doc.isEmpty()) {
            return ResponseEntity.status(404)
                    .body(new ErrorResponseDTO("No Doctor found with id: " + id, 404));
        }
        doctorService.deleteDoctorByID(id);
        return ResponseEntity.noContent().build(); // 204

    }

    @PutMapping("/{id}")
    @Operation(summary = "Update Doctor Details", description = "Update full information of an existing doctor by ID, [Requires Role: ADMIN]")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Doctor Updated Successfully", content = @Content(schema = @Schema(implementation = Doctor.class))),
            @ApiResponse(responseCode = "400", description = "Invalid ID or request body validation failed", content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized, Missing or invalid JWT token", content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))),
            @ApiResponse(responseCode = "403", description = "Forbidden, Requires ADMIN role", content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "No doctor found with that ID", content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class)))
    })
    public ResponseEntity<?> updateDoctor(
            @Parameter(description = "ID of the doctor to update", example = "1") @PathVariable @Positive(message = "ID must be a positive number") int id,
            @Valid @RequestBody Doctor doc) {
        Optional<Doctor> existingDoc = doctorService.getDoctorByID(id);
        if (existingDoc.isEmpty()) {
            return ResponseEntity.status(404)
                    .body(new ErrorResponseDTO("No Doctor found with id: " + id, 404));
        }
        doc.setId(id);
        doc.setHireDate(existingDoc.get().getHireDate());
        doctorService.updateDoctorById(id, doc);
        return ResponseEntity.ok(doc); // 200
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Partially Update A Doctor", description = "Partially update specific fields (like firstName, email, specialty, or salary) of an existing doctor by ID, [Requires Role: ADMIN]")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Doctor Partially Updated Successfully", content = @Content(schema = @Schema(implementation = Doctor.class))),
            @ApiResponse(responseCode = "400", description = "ID must be a positive number", content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized, Missing or invalid JWT token", content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))),
            @ApiResponse(responseCode = "403", description = "Forbidden, Requires ADMIN role", content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "No doctor found with that ID", content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class)))
    })
    public ResponseEntity<?> partialDocUpdate(
            @Parameter(description = "ID of the doctor to partially update", example = "1") @PathVariable @Positive(message = "ID must be a positive number") int id,
            @RequestBody Map<String, Object> toUpdate) {
        Optional<Doctor> existingDoc = doctorService.getDoctorByID(id);
        if (existingDoc.isEmpty()) {
            return ResponseEntity.status(404)
                    .body(new ErrorResponseDTO("No Doctor found with id: " + id, 404));
        }
        if (toUpdate.containsKey("firstName"))
            existingDoc.get().setFirstName((String) toUpdate.get("firstName"));
        if (toUpdate.containsKey("lastName"))
            existingDoc.get().setLastName((String) toUpdate.get("lastName"));
        if (toUpdate.containsKey("email"))
            existingDoc.get().setEmail((String) toUpdate.get("email"));
        if (toUpdate.containsKey("specialty"))
            existingDoc.get().setSpecialty((String) toUpdate.get("specialty"));
        if (toUpdate.containsKey("salary"))
            existingDoc.get().setSalary(new BigDecimal(toUpdate.get("salary").toString()));
        doctorService.updateDoctorById(id, existingDoc.get());
        return ResponseEntity.ok(existingDoc.get()); // 200
    }

    @GetMapping("/specialty")
    @Operation(summary = "Get Doctors By Specialty", description = "Retrieve a paginated list of doctors filtered by their medical specialty")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success", content = @Content(schema = @Schema(implementation = PaginatedListDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid parameters: page must be >= 0 and size must be between 1 and 50", content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized, Missing or invalid JWT token", content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))),
            @ApiResponse(responseCode = "403", description = "Forbidden, Insufficient permissions", content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class)))
    })
    public ResponseEntity<?> getDoctorsBySpecialty(
            @Parameter(description = "Medical specialty to filter by", example = "Cardiology") @RequestParam String specialty,
            @Parameter(description = "Page index starting from zero", example = "0") @RequestParam(defaultValue = "0") @Min(value = 0, message = "Page Number Must Be Positive") int page,
            @Parameter(description = "Number of doctors per page (1-50)", example = "5") @RequestParam(defaultValue = "5") @Min(value = 1, message = "Size must be between 1 and 50") @Max(value = 50, message = "Size must be between 1 and 50") int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<Doctor> docPage = doctorService.findDoctorsBySpecialty(specialty, pageable);

        long total = docPage.getTotalElements();
        PaginatedListDTO<Doctor> response = new PaginatedListDTO<>(
                docPage.getContent(),
                page,
                size,
                total);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/search")
    @Operation(summary = "Search Doctors", description = "Search and filter doctors dynamically by optional specialty and/or salary")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success", content = @Content(schema = @Schema(implementation = PaginatedListDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid pagination parameters", content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized, Missing or invalid JWT token", content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))),
            @ApiResponse(responseCode = "403", description = "Forbidden, Insufficient permissions", content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class)))
    })
    public ResponseEntity<?> searchDoctors(
            @Parameter(description = "Optional specialty filter", example = "Cardiology") @RequestParam(required = false) String specialty,
            @Parameter(description = "Optional salary filter", example = "12000.00") @RequestParam(required = false) BigDecimal salary,
            @Parameter(description = "Page index starting from zero", example = "0") @RequestParam(defaultValue = "0") @Min(value = 0, message = "Page Number Must Be Positive") int page,
            @Parameter(description = "Number of doctors per page (1-50)", example = "5") @RequestParam(defaultValue = "5") @Min(value = 1, message = "Size must be between 1 and 50") @Max(value = 50, message = "Size must be between 1 and 50") int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<Doctor> docPage = doctorService.findDoctorsBySpecialtyAndSalary(specialty, salary, pageable);

        long total = docPage.getTotalElements();
        PaginatedListDTO<Doctor> response = new PaginatedListDTO<>(
                docPage.getContent(),
                page,
                size,
                total);

        return ResponseEntity.ok(response);
    }

}
