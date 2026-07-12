package com.example.clinicmvcspring.controllers;

import org.springframework.web.bind.annotation.RestController;

import com.example.clinicmvcspring.dtos.SignupRequest;
import com.example.clinicmvcspring.models.AppUser;
import com.example.clinicmvcspring.services.AppUserDetailsService;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {
    private final AppUserDetailsService userService;

    public UserController(AppUserDetailsService userService) {
        this.userService = userService;
    }

    @PostMapping("signup")
    public ResponseEntity<?> registerUser(@RequestBody SignupRequest request) {

        AppUser newUser = new AppUser();
        newUser.setUsername(request.getUsername());
        newUser.setPassword(request.getPassword());
        newUser.setRole(request.getRole());
        newUser.setEnabled(true);
        userService.addUser(newUser);
        return new ResponseEntity<>(
                Map.of(
                        "status", 201,
                        "message", "User registered successfully!"),
                HttpStatus.CREATED);
    }

}
