package com.example.clinicmvcspring.exceptions;

import com.example.clinicmvcspring.dtos.ErrorResponseDTO;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

// cathc exceptions for all rest controllers
@RestControllerAdvice
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

    // Server error
    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleGeneral(Exception e) {
        ErrorResponseDTO error = new ErrorResponseDTO("Unexpected error: " + e.getMessage(), 500);
        return ResponseEntity.status(500).body(error);
    }
}
