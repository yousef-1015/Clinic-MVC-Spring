
package com.example.clinicmvcspring.dtos;

import java.sql.Timestamp;

import io.swagger.v3.oas.annotations.media.Schema;

public class PrescriptionDTO {
    @Schema(description = "Unique ID of the prescription", example = "1")
    private Integer id;
    @Schema(description = "Doctor notes", example = "Take 1 pill twice daily after meals")
    private String prescriptionNotes;
    @Schema(description = "Creation timestamp", example = "2026-07-22T10:00:00Z")
    private Timestamp createdAt;

    public PrescriptionDTO() {
    }

    public PrescriptionDTO(Integer id, String prescriptionNotes, Timestamp createdAt) {
        this.id = id;
        this.prescriptionNotes = prescriptionNotes;
        this.createdAt = createdAt;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPrescriptionNotes() {
        return prescriptionNotes;
    }

    public void setPrescriptionNotes(String prescriptionNotes) {
        this.prescriptionNotes = prescriptionNotes;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }
}
