package com.example.clinicmvcspring.exceptions;

import java.util.NoSuchElementException;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.example.clinicmvcspring.dtos.ErrorResponseDTO;

import lombok.extern.slf4j.Slf4j;

// cathc exceptions for all rest controllers
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    // Catches @Valid
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidationErrors(MethodArgumentNotValidException e) {

        String errorMessage = e.getFieldErrors().get(0).getDefaultMessage();

        ErrorResponseDTO error = new ErrorResponseDTO(errorMessage, 400);
        return ResponseEntity.status(400).body(error); // 400
    }

    // Duplicate email
    @ExceptionHandler(DuplicateKeyException.class)
    public ResponseEntity<?> handleDuplicateKey(DuplicateKeyException e) {
        ErrorResponseDTO error = new ErrorResponseDTO("A record with this email already exists", 409);
        return ResponseEntity.status(409).body(error);
    }

    // db constraint violations
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<?> handleDataIntegrity(DataIntegrityViolationException e) {
        ErrorResponseDTO error = new ErrorResponseDTO("Cannot complete: data integrity violation", 409);
        return ResponseEntity.status(409).body(error);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<?> handleBadCredentials(BadCredentialsException e) {
        ErrorResponseDTO error = new ErrorResponseDTO("Invalid username or password", 401);
        return ResponseEntity.status(401).body(error);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<?> handleIllegalArgumentException(IllegalArgumentException e) {
        ErrorResponseDTO error = new ErrorResponseDTO("Invalid value: " + e.getMessage(), 400);
        return ResponseEntity.status(400).body(error);
    }

    @ExceptionHandler(NumberFormatException.class)
    public ResponseEntity<?> handleNumberFormatException(NumberFormatException e) {
        ErrorResponseDTO error = new ErrorResponseDTO("Invalid number format", 400);
        return ResponseEntity.status(400).body(error);
    }

    @ExceptionHandler(ClassCastException.class)
    public ResponseEntity<?> handleClassCastException(ClassCastException e) {
        ErrorResponseDTO error = new ErrorResponseDTO("Invalid data type in request", 400);
        return ResponseEntity.status(400).body(error);
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<?> handleNoSuchElementException(NoSuchElementException e) {
        ErrorResponseDTO error = new ErrorResponseDTO("Resource not found", 404);
        return ResponseEntity.status(404).body(error);
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<?> handleUsernameNotFound(UsernameNotFoundException e) {
        ErrorResponseDTO error = new ErrorResponseDTO("User not found", 401);
        return ResponseEntity.status(401).body(error);
    }

    // Server error
    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleGeneral(Exception e) {

        log.error("Unexpected server error occurred", e);// from the @Slf4j in the lombok library

        ErrorResponseDTO error = new ErrorResponseDTO("Internal Server Error", 500);
        return ResponseEntity.status(500).body(error);
    }
}
