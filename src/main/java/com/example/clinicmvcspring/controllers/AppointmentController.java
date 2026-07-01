package com.example.clinicmvcspring.controllers;

import org.springframework.web.bind.annotation.RestController;

import com.example.clinicmvcspring.models.Appointment;
import com.example.clinicmvcspring.services.AppointmentService;

import java.sql.Timestamp;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
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
    public List<Appointment> getAppointments() {
        return appointmentService.getAllAppointments();
    }

    @GetMapping("/{id}")
    public Object getAppointmentByID(@PathVariable int id) {
        Map<String, Object> response = new LinkedHashMap<>();

        try {
            Appointment app = appointmentService.getAppointmentByID(id);
            if (app == null) {
                response.put("message", "ERROR: Appointment not found");
                response.put("idRequested", id);
                return response;
            }
            return app;
        } catch (Exception e) {
            response.put("message", "ERROR searching for appointment");
            response.put("reason", e.getMessage());
            return response;
        }
    }

    @PostMapping
    public Map<String, Object> addNewAppointment(@RequestBody Appointment newApp) {
        Map<String, Object> response = new LinkedHashMap<>();

        try {
            appointmentService.addAppointment(newApp);
            response.put("message", "Appointment added successfully !!!");
            response.put("dateAndTime", newApp.getDateAndTime());
            response.put("patientId", newApp.getPatientId());
            response.put("doctorId", newApp.getDoctorId());
            return response;
        } catch (Exception e) {
            response.put("message", "ERROR adding appointment");
            response.put("Reason", e.getMessage());
            return response;
        }
    }

    @DeleteMapping("/{id}")
    public Map<String, Object> deleteAppointment(@PathVariable int id) {
        Map<String, Object> response = new LinkedHashMap<>();

        try {
            Appointment app = appointmentService.getAppointmentByID(id);

            if (app == null) {
                response.put("message", "ERROR: Appointment not found");
                response.put("idRequested", id);
                return response;
            }
            boolean isDeleted = appointmentService.deleteAppointmentByID(id);

            if (isDeleted == false) {
                response.put("message", "ERROR: Appointment was'nt Deleted");
                response.put("idRequested", id);
                return response;
            }
            response.put("message", "Appointment Deleted Successfully !!!!");
            response.put("idRequested", id);
            return response;
        } catch (Exception e) {
            response.put("message", "ERROR DELETING appointment");
            if (e.getMessage() != null && e.getMessage().toLowerCase().contains("foreign key")) {
                response.put("reason", "This appointment has a prescription and cannot be deleted");
            } else {
                response.put("reason", e.getMessage());
            }
            return response;
        }
    }

    @PutMapping("/{id}")
    public Map<String, Object> updateAppointment(@PathVariable int id, @RequestBody Appointment app) {
        Map<String, Object> response = new LinkedHashMap<>();

        try {
            Appointment existingApp = appointmentService.getAppointmentByID(id);

            if (existingApp == null) {
                response.put("message", "ERROR: Appointment not found");
                response.put("idRequested", id);
                return response;
            }

            appointmentService.updateAppointmentById(id, app);

            response.put("message", "Appointment Updated Successfully !!!");
            response.put("dateAndTime", app.getDateAndTime());
            response.put("patientId", app.getPatientId());
            response.put("doctorId", app.getDoctorId());
            response.put("status", app.getStatus());
            response.put("created date", existingApp.getCreatedAt());

            return response;
        } catch (Exception e) {
            response.put("message", "ERROR Updating appointment");
            response.put("Reason", e.getMessage());
            return response;
        }
    }

    @PatchMapping("/{id}")
    public Map<String, Object> partialAppointmentUpdate(@PathVariable int id,
            @RequestBody Map<String, Object> toUpdate) {
        Map<String, Object> response = new LinkedHashMap<>();

        try {
            Appointment existingApp = appointmentService.getAppointmentByID(id);

            if (existingApp == null) {
                response.put("message", "ERROR: Appointment not found");
                response.put("idRequested", id);
                return response;
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
                existingApp.setStatus((String) toUpdate.get("status"));
            }

            appointmentService.updateAppointmentById(id, existingApp);

            response.put("message", "Appointment Updated Successfully !!!");
            response.put("dateAndTime", existingApp.getDateAndTime());
            response.put("patientId", existingApp.getPatientId());
            response.put("doctorId", existingApp.getDoctorId());
            response.put("status", existingApp.getStatus());
            response.put("created date", existingApp.getCreatedAt());

            return response;
        } catch (Exception e) {
            response.put("message", "ERROR Updating appointment");
            response.put("Reason", e.getMessage());
            return response;
        }
    }
}
