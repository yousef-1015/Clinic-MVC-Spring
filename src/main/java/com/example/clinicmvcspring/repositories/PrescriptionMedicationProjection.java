package com.example.clinicmvcspring.repositories;

public interface PrescriptionMedicationProjection {
    // get the values in the prescriptions_medications table

    int getMedicationId();

    String getMedicationName();

    String getDosage();

    String getFrequency();

}