package com.example.clinicmvcspring.controllers;

import java.math.BigDecimal;
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

@RestController
@RequestMapping("/api/v1/doctors")
@Tag(name = "Doctor Management", description = "Make All CRUD Operations On Doctors")
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
            @RequestParam(defaultValue = "0") @Parameter(description = "Page index starting from zero", example = "0") int page,
            @Parameter(description = "Number of doctors per page (1-50)", example = "5") @RequestParam(defaultValue = "5") int size) {

        if (page < 0) {
            ErrorResponseDTO error = new ErrorResponseDTO("Page Number Must Be Positive", 400);
            return ResponseEntity.status(400).body(error);
        }
        if (size <= 0 || size > 50) {
            ErrorResponseDTO error = new ErrorResponseDTO("Size must be between 1 and 50", 400);
            return ResponseEntity.status(400).body(error);
        }

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
            @Parameter(description = "Database ID of the doctor to retrieve", example = "1") @PathVariable int id) {
        if (id <= 0) {
            return ResponseEntity.status(400)
                    .body(new ErrorResponseDTO("ID must be a positive number", 400));
        }
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
            @ApiResponse(responseCode = "201", description = "Doctor Creates Successfully", content = @Content(schema = @Schema(implementation = Doctor.class))),
            @ApiResponse(responseCode = "409", description = "Email Already Used", content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized, Missing or invalid JWT token", content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))),
            @ApiResponse(responseCode = "403", description = "Forbidden, Requires ADMIN role", content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request body", content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class)))

    })
    public ResponseEntity<?> addNewDoctor(@Valid @RequestBody Doctor newDoc) {
        return ResponseEntity.status(201).body(doctorService.addDoctor(newDoc)); // 201

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteDoctor(@PathVariable int id) {
        if (id <= 0) {
            return ResponseEntity.status(400)
                    .body(new ErrorResponseDTO("ID must be a positive number", 400));
        }
        Optional<Doctor> doc = doctorService.getDoctorByID(id);
        if (doc.isEmpty()) {
            return ResponseEntity.status(404)
                    .body(new ErrorResponseDTO("No Doctor found with id: " + id, 404));
        }
        doctorService.deleteDoctorByID(id);
        return ResponseEntity.noContent().build(); // 204

    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateDoctor(@PathVariable int id, @Valid @RequestBody Doctor doc) {
        if (id <= 0) {
            return ResponseEntity.status(400)
                    .body(new ErrorResponseDTO("ID must be a positive number", 400));
        }
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
    public ResponseEntity<?> partialDocUpdate(@PathVariable int id, @RequestBody Map<String, Object> toUpdate) {
        if (id <= 0) {
            return ResponseEntity.status(400)
                    .body(new ErrorResponseDTO("ID must be a positive number", 400));
        }
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
    public ResponseEntity<?> getDoctorsBySpecialty(
            @RequestParam String specialty,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {

        if (page < 0) {
            ErrorResponseDTO error = new ErrorResponseDTO("Page Number Must Be Positive", 400);
            return ResponseEntity.status(400).body(error);
        }
        if (size <= 0 || size > 50) {
            ErrorResponseDTO error = new ErrorResponseDTO("Size must be between 1 and 50", 400);
            return ResponseEntity.status(400).body(error);
        }

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
    public ResponseEntity<?> searchDoctors(
            @RequestParam(required = false) String specialty,
            @RequestParam(required = false) BigDecimal salary,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {

        if (page < 0) {
            ErrorResponseDTO error = new ErrorResponseDTO("Page Number Must Be Positive", 400);
            return ResponseEntity.status(400).body(error);
        }
        if (size <= 0 || size > 50) {
            ErrorResponseDTO error = new ErrorResponseDTO("Size must be between 1 and 50", 400);
            return ResponseEntity.status(400).body(error);
        }

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
