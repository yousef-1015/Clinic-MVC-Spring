package com.example.clinicmvcspring.controllers;

import java.sql.Timestamp;
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

import com.example.clinicmvcspring.dtos.AppointmentDTO;
import com.example.clinicmvcspring.dtos.ErrorResponseDTO;
import com.example.clinicmvcspring.dtos.PaginatedListDTO;
import com.example.clinicmvcspring.models.Appointment;
import com.example.clinicmvcspring.models.AppointmentStatus;
import com.example.clinicmvcspring.services.AppointmentService;

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
@RequestMapping("/api/v1/appointments")
@Tag(name = "Appointment Management", description = "Make All CRUD Operations On Appointments")
@Validated
public class AppointmentController {
    private final AppointmentService appointmentService;

    public AppointmentController(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    @GetMapping
    @Operation(summary = "Get All Appointments", description = "Retrieve All Appointments From The Database, [Requires Role: ADMIN]")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success", content = @Content(schema = @Schema(implementation = PaginatedListDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid parameters: page must be >= 0 and size must be between 1 and 50", content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized, Missing or invalid JWT token", content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))),
            @ApiResponse(responseCode = "403", description = "Forbidden, Requires ADMIN role", content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class)))
    })
    public ResponseEntity<?> getAppointments(
            @Parameter(description = "Page index starting from zero", example = "0") @RequestParam(defaultValue = "0") @Min(value = 0, message = "Page Number Must Be Positive") int page,
            @Parameter(description = "Number of appointments per page (1-50)", example = "5") @RequestParam(defaultValue = "5") @Min(value = 1, message = "Size must be between 1 and 50") @Max(value = 50, message = "Size must be between 1 and 50") int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<AppointmentDTO> appPage = appointmentService.getAllAppointments(pageable);
        long total = appPage.getTotalElements();
        PaginatedListDTO<AppointmentDTO> response = new PaginatedListDTO<>(appPage.getContent(), page, size, total);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get A Single Appointment", description = "Retrieve a Single Appointment By ID, [Requires Role: ADMIN]")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success", content = @Content(schema = @Schema(implementation = AppointmentDTO.class))),
            @ApiResponse(responseCode = "400", description = "ID must be a positive number", content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized, Missing or invalid JWT token", content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))),
            @ApiResponse(responseCode = "403", description = "Forbidden, Requires ADMIN role", content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "No appointment found with that ID", content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class)))
    })
    public ResponseEntity<?> getAppointmentByID(
            @Parameter(description = "Database ID of the appointment to retrieve", example = "1") @PathVariable @Positive(message = "ID must be a positive number") int id) {

        Optional<AppointmentDTO> app = appointmentService.getAppointmentByID(id);
        if (app.isEmpty()) {
            return ResponseEntity.status(404)
                    .body(new ErrorResponseDTO("No appointment found with id: " + id, 404));
        }

        return ResponseEntity.ok(app.get());

    }

    @PostMapping
    @Operation(summary = "Add A New Appointment", description = "Insert A New Appointment Into The Database, [Requires Role: ADMIN]")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Appointment Created Successfully", content = @Content(schema = @Schema(implementation = AppointmentDTO.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized, Missing or invalid JWT token", content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))),
            @ApiResponse(responseCode = "403", description = "Forbidden, Requires ADMIN role", content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request body", content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class)))
    })
    public ResponseEntity<?> addNewAppointment(@Valid @RequestBody Appointment newApp) {

        return ResponseEntity.status(201).body(appointmentService.addAppointment(newApp));

    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete An Appointment", description = "Delete an Appointment From The Database Based on Id, [Requires Role: ADMIN]")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Appointment Deleted Successfully", content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized, Missing or invalid JWT token", content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))),
            @ApiResponse(responseCode = "403", description = "Forbidden, Requires ADMIN role", content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "No appointment found with that ID", content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "ID must be a positive number", content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class)))
    })
    public ResponseEntity<?> deleteAppointment(
            @Parameter(description = "ID of the appointment to delete", example = "1") @PathVariable @Positive(message = "ID must be a positive number") int id) {

        Optional<Appointment> app = appointmentService.getAppointmentEntityByID(id);

        if (app.isEmpty()) {
            return ResponseEntity.status(404)
                    .body(new ErrorResponseDTO("No Appointment found with id: " + id, 404));
        }
        appointmentService.deleteAppointmentByID(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update Appointment Details", description = "Update full information of an existing appointment by ID, [Requires Role: ADMIN]")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Appointment Updated Successfully", content = @Content(schema = @Schema(implementation = AppointmentDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid ID or request body validation failed", content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized, Missing or invalid JWT token", content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))),
            @ApiResponse(responseCode = "403", description = "Forbidden, Requires ADMIN role", content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "No appointment found with that ID", content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class)))
    })
    public ResponseEntity<?> updateAppointment(
            @Parameter(description = "ID of the appointment to update", example = "1") @PathVariable @Positive(message = "ID must be a positive number") int id,
            @Valid @RequestBody Appointment app) {

        Optional<AppointmentDTO> existingApp = appointmentService.getAppointmentByID(id);

        if (existingApp.isEmpty()) {
            return ResponseEntity.status(404)
                    .body(new ErrorResponseDTO("No Appointment found with id: " + id, 404));
        }

        app.setId(id);
        app.setCreatedAt(existingApp.get().getCreatedAt());

        return ResponseEntity.ok(appointmentService.updateAppointmentById(id, app));

    }

    @PatchMapping("/{id}")
    @Operation(summary = "Partially Update An Appointment", description = "Partially update specific fields (like dateAndTime, status, patientId, doctorId) of an existing appointment by ID, [Requires Role: ADMIN]")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Appointment Partially Updated Successfully", content = @Content(schema = @Schema(implementation = AppointmentDTO.class))),
            @ApiResponse(responseCode = "400", description = "ID must be a positive number", content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized, Missing or invalid JWT token", content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))),
            @ApiResponse(responseCode = "403", description = "Forbidden, Requires ADMIN role", content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "No appointment found with that ID", content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class)))
    })
    public ResponseEntity<?> partialAppointmentUpdate(
            @Parameter(description = "ID of the appointment to partially update", example = "1") @PathVariable @Positive(message = "ID must be a positive number") int id,
            @RequestBody Map<String, Object> toUpdate) {

        Optional<Appointment> existingApp = appointmentService.getAppointmentEntityByID(id);

        if (existingApp.isEmpty()) {
            return ResponseEntity.status(404)
                    .body(new ErrorResponseDTO("No Appointment found with id: " + id, 404));
        }

        if (toUpdate.containsKey("dateAndTime")) {
            String dtStr = (String) toUpdate.get("dateAndTime");
            existingApp.get().setDateAndTime(Timestamp.valueOf(dtStr));
        }

        if (toUpdate.containsKey("patientId")) {
            existingApp.get().setPatientId(((Number) toUpdate.get("patientId")).intValue());
        }
        if (toUpdate.containsKey("doctorId")) {
            existingApp.get().setDoctorId(((Number) toUpdate.get("doctorId")).intValue());
        }
        if (toUpdate.containsKey("status")) {
            existingApp.get().setStatus(AppointmentStatus.valueOf((String) toUpdate.get("status")));
        }

        return ResponseEntity.ok(appointmentService.updateAppointmentById(id, existingApp.get()));

    }

    @GetMapping("status")
    @Operation(summary = "Get Appointments By Status", description = "Retrieve a paginated list of appointments filtered by status (e.g. SCHEDULED, COMPLETED, CANCELLED)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success", content = @Content(schema = @Schema(implementation = PaginatedListDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid pagination parameters", content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized, Missing or invalid JWT token", content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))),
            @ApiResponse(responseCode = "403", description = "Forbidden, Insufficient permissions", content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class)))
    })
    public ResponseEntity<?> getAppointmentByStatus(
            @Parameter(description = "Page index starting from zero", example = "0") @RequestParam(defaultValue = "0") @Min(value = 0, message = "Page Number Must Be Positive") int page,
            @Parameter(description = "Number of appointments per page (1-50)", example = "5") @RequestParam(defaultValue = "5") @Min(value = 1, message = "Size must be between 1 and 50") @Max(value = 50, message = "Size must be between 1 and 50") int size,
            @Parameter(description = "Appointment status to filter by", example = "SCHEDULED") @RequestParam AppointmentStatus status) {

        Pageable pageable = PageRequest.of(page, size);
        Page<AppointmentDTO> appPage = appointmentService.findAppointmentByStatus(status, pageable);

        long total = appPage.getTotalElements();
        PaginatedListDTO<AppointmentDTO> response = new PaginatedListDTO<>(
                appPage.getContent(),
                page,
                size,
                total);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/search/date")
    @Operation(summary = "Get Appointments By Date Range", description = "Retrieve a paginated list of appointments within an optional start and end timestamp range")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success", content = @Content(schema = @Schema(implementation = PaginatedListDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid pagination parameters", content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized, Missing or invalid JWT token", content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))),
            @ApiResponse(responseCode = "403", description = "Forbidden, Insufficient permissions", content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class)))
    })
    public ResponseEntity<?> getAppointmentsByDateRange(
            @Parameter(description = "Optional start timestamp filter", example = "2026-07-01T00:00:00Z") @RequestParam(required = false) Timestamp start,
            @Parameter(description = "Optional end timestamp filter", example = "2026-07-31T23:59:59Z") @RequestParam(required = false) Timestamp end,
            @Parameter(description = "Page index starting from zero", example = "0") @RequestParam(defaultValue = "0") @Min(value = 0, message = "Page Number Must Be Positive") int page,
            @Parameter(description = "Number of appointments per page (1-50)", example = "5") @RequestParam(defaultValue = "5") @Min(value = 1, message = "Size must be between 1 and 50") @Max(value = 50, message = "Size must be between 1 and 50") int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<AppointmentDTO> appPage = appointmentService.findAppointmentByDate(start, end, pageable);

        long total = appPage.getTotalElements();
        PaginatedListDTO<AppointmentDTO> response = new PaginatedListDTO<>(
                appPage.getContent(),
                page,
                size,
                total);

        return ResponseEntity.ok(response);
    }

    @PatchMapping("/cancel/{id}")
    @Operation(summary = "Cancel an Appointment", description = "Setting the status of an already booked appointment to Canceled, [Requires Role: ADMIN]")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Appointment Canceled Successfully", content = @Content(schema = @Schema(implementation = AppointmentDTO.class))),
            @ApiResponse(responseCode = "400", description = "ID must be a positive number", content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized, Missing or invalid JWT token", content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))),
            @ApiResponse(responseCode = "403", description = "Forbidden, Requires ADMIN role", content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "No appointment found with that ID", content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class)))
    })
    public ResponseEntity<?> cancelAppointment(
            @Parameter(description = "ID of the appointment to cacncel", example = "1") @PathVariable @Positive(message = "ID must be a positive number") int id) {

        Optional<Appointment> existingApp = appointmentService.getAppointmentEntityByID(id);

        if (existingApp.isEmpty()) {
            return ResponseEntity.status(404)
                    .body(new ErrorResponseDTO("No Appointment found with id: " + id, 404));
        }
        return ResponseEntity.ok(appointmentService.cancelAppointment(id));

    }

}
