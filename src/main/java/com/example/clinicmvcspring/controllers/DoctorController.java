package com.example.clinicmvcspring.controllers;

import org.springframework.web.bind.annotation.RestController;

import com.example.clinicmvcspring.models.DoctorModel;
import com.example.clinicmvcspring.services.DoctorService;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
@RequestMapping("/api/v1/doctors")
public class DoctorController {
    private final DoctorService doctorService;

    public DoctorController(DoctorService doctorService) {
        this.doctorService = doctorService;
    }

    // Get all doctors as JSON at
    @GetMapping
    public List<DoctorModel> getDoctors() {
        return doctorService.getAllDoctors();
    }
}
