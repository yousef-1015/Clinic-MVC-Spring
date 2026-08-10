package com.example.clinicmvcspring.exceptions;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.example.clinicmvcspring.dtos.ErrorResponseDTO;

import jakarta.validation.ConstraintViolationException;

@RestControllerAdvice
//this is needed to tell spring thr order of checking
@Order(Ordered.HIGHEST_PRECEDENCE)// run before the global exception handler 
public class ValidationExceptionHandler {

    // Handles @Positive, @Min, @Max violations on @PathVariable and @RequestParam
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<?> handleConstraintViolation(ConstraintViolationException e) {
        String message = e.getConstraintViolations()
                .iterator()
                .next()
                .getMessage();
        ErrorResponseDTO error = new ErrorResponseDTO(message, 400);
        return ResponseEntity.status(400).body(error);
    }

}
