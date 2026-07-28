package com.example.clinicmvcspring.events;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class DoctorUpdatedEvent {
    private final String doctorName;
    private final String message;
}
