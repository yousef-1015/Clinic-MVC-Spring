package com.example.clinicmvcspring.controllers;

import org.springframework.web.bind.annotation.RestController;

import com.example.clinicmvcspring.dtos.ErrorResponseDTO;
import com.example.clinicmvcspring.dtos.PaginatedListDTO;
import com.example.clinicmvcspring.dtos.SignupRequest;
import com.example.clinicmvcspring.models.AppUser;
import com.example.clinicmvcspring.models.Doctor;
import com.example.clinicmvcspring.models.Role;
import com.example.clinicmvcspring.services.AppUserDetailsService;
import com.example.clinicmvcspring.services.DoctorService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;

import java.util.Map;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {
    private final AppUserDetailsService userService;
    private final DoctorService doctorService;

    public UserController(AppUserDetailsService userService, DoctorService doctorService) {
        this.userService = userService;
        this.doctorService = doctorService;
    }

    @GetMapping("")
    public ResponseEntity<?> getAllUsers(@RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {
        if (page < 0) {
            ErrorResponseDTO error = new ErrorResponseDTO("Page Number can't be negative", 400);
            return ResponseEntity.status(400).body(error);
        }
        if (size <= 0 || size > 50) {
            ErrorResponseDTO error = new ErrorResponseDTO("Size must be between 1 and 50", 400);
            return ResponseEntity.status(400).body(error);
        }
        Pageable pageable = PageRequest.of(page, size);

        Page<AppUser> userPage = userService.getAllUsers(pageable);
        long total = userPage.getTotalElements();
        PaginatedListDTO<AppUser> response = new PaginatedListDTO<>(userPage.getContent(), page, size, total);
        return ResponseEntity.ok(response);
    }

    @PostMapping("signup")
    public ResponseEntity<?> registerUser(@RequestBody SignupRequest request) {

        AppUser newUser = new AppUser();
        newUser.setUsername(request.getUsername());
        newUser.setPassword(request.getPassword());
        newUser.setRole(request.getRole());
        newUser.setEnabled(true);
        AppUser savedUser = userService.addUser(newUser);
        if (request.getRole() == Role.DOCTOR) {
            if (request.getForeignId() != null) {
                Optional<Doctor> docOp = doctorService.getDoctorByID(request.getForeignId());
                if (docOp.isPresent()) {
                    Doctor doctor = docOp.get();
                    doctor.setUserId(savedUser.getId());
                    doctorService.updateDoctorById(doctor.getId(), doctor);
                } else {
                    return new ResponseEntity<>(
                            Map.of(
                                    "status", 201,
                                    "message", "User registered successfully!"),
                            HttpStatus.CREATED);
                }
            }

        }
        return new ResponseEntity<>(
                Map.of(
                        "status", 201,
                        "message", "User registered successfully!"),
                HttpStatus.CREATED);
    }

}
