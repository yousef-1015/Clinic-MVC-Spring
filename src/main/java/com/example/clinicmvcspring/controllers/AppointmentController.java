package com.example.clinicmvcspring.controllers;

import org.springframework.web.bind.annotation.RestController;

import com.example.clinicmvcspring.dtos.ErrorResponseDTO;
import com.example.clinicmvcspring.models.Appointment;
import com.example.clinicmvcspring.models.AppointmentStatus;
import com.example.clinicmvcspring.services.AppointmentService;

import jakarta.validation.Valid;

import java.sql.Timestamp;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
@RequestMapping("/api/v1/appointments")
public class AppointmentController {
    private final AppointmentService appointmentService;

    public AppointmentController(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    @GetMapping
    public ResponseEntity<?> getAppointments(@RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {
        if (page < 0) {
            ErrorResponseDTO error = new ErrorResponseDTO("Page Number Must Be Positive", 400);
            return ResponseEntity.status(400).body(error);
        }
        if (size <= 0 || size > 50) {
            ErrorResponseDTO error = new ErrorResponseDTO("Size must be between 1 and 50", 400);
            return ResponseEntity.status(400).body(error);
        }
        List<Appointment> allApps = appointmentService.getAllAppointments(page, size);
        int total = appointmentService.countAppointments();
        int totalPages = (int) Math.ceil((double) total / size);
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("data", allApps);
        response.put("currentPage", page);
        response.put("pageSize", size);
        response.put("totalAppointments", total);
        response.put("totalPages", totalPages);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getAppointmentByID(@PathVariable int id) {
        if (id <= 0) {
            return ResponseEntity.status(400).body(new ErrorResponseDTO("ID must be a positive number", 400));
        }

        Appointment app = appointmentService.getAppointmentByID(id);
        if (app == null) {
            return ResponseEntity.status(404)
                    .body(new ErrorResponseDTO("No appointment found with id: " + id, 404));
        }

        return ResponseEntity.ok(app);

    }

    @PostMapping
    public ResponseEntity<?> addNewAppointment(@Valid @RequestBody Appointment newApp) {

        appointmentService.addAppointment(newApp);
        return ResponseEntity.status(201).body(newApp);

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteAppointment(@PathVariable int id) {
        if (id <= 0) {
            return ResponseEntity.status(400)
                    .body(new ErrorResponseDTO("ID must be a positive number", 400));
        }
        Appointment app = appointmentService.getAppointmentByID(id);

        if (app == null) {
            return ResponseEntity.status(404)
                    .body(new ErrorResponseDTO("No Appointment found with id: " + id, 404));
        }
        appointmentService.deleteAppointmentByID(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateAppointment(@PathVariable int id, @Valid @RequestBody Appointment app) {
        if (id <= 0) {
            return ResponseEntity.status(400)
                    .body(new ErrorResponseDTO("ID must be a positive number", 400));
        }
        Appointment existingApp = appointmentService.getAppointmentByID(id);

        if (existingApp == null) {
            return ResponseEntity.status(404)
                    .body(new ErrorResponseDTO("No Appointment found with id: " + id, 404));
        }

        app.setId(id);
        app.setCreatedAt(existingApp.getCreatedAt());
        appointmentService.updateAppointmentById(id, app);
        return ResponseEntity.ok(app);

    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> partialAppointmentUpdate(@PathVariable int id,
            @RequestBody Map<String, Object> toUpdate) {
        if (id <= 0) {
            return ResponseEntity.status(400)
                    .body(new ErrorResponseDTO("ID must be a positive number", 400));
        }
        Appointment existingApp = appointmentService.getAppointmentByID(id);

        if (existingApp == null) {
            return ResponseEntity.status(404)
                    .body(new ErrorResponseDTO("No Appointment found with id: " + id, 404));
        }

        if (toUpdate.containsKey("dateAndTime")) {
            String dtStr = (String) toUpdate.get("dateAndTime");
            existingApp.setDateAndTime(Timestamp.valueOf(dtStr));
        }

        if (toUpdate.containsKey("patientId")) {
            existingApp.setPatientId(((Number) toUpdate.get("patientId")).intValue());
        }
        if (toUpdate.containsKey("doctorId")) {
            existingApp.setDoctorId(((Number) toUpdate.get("doctorId")).intValue());
        }
        if (toUpdate.containsKey("status")) {
            existingApp.setStatus(AppointmentStatus.valueOf((String) toUpdate.get("status")));
        }

        appointmentService.updateAppointmentById(id, existingApp);

        return ResponseEntity.ok(existingApp);

    }
}
