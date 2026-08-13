package com.example.clinicmvcspring.services;

import java.io.ByteArrayOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import com.example.clinicmvcspring.config.RabbitMQConstants;
import com.example.clinicmvcspring.models.Appointment;
import com.example.clinicmvcspring.repositories.AppointmentRepo;
import com.example.clinicmvcspring.repositories.PrescriptionMedicationProjection;
import com.example.clinicmvcspring.repositories.PrescriptionRepo;
import com.lowagie.text.Document;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfWriter;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class PdfService {
    private final PrescriptionRepo prescriptionRepo;
    private final AppointmentRepo appointmentRepo;

    public PdfService(PrescriptionRepo prescriptionRepo, AppointmentRepo appointmentRepo) {
        this.prescriptionRepo = prescriptionRepo;
        this.appointmentRepo = appointmentRepo;
    }

    private byte[] generateAppointmentReport(Appointment app) {

        ByteArrayOutputStream out = new ByteArrayOutputStream();

        Document document = new Document();

        try {
            PdfWriter.getInstance(document, out);

            document.open();

            // THIS IS THE TEMPLATE!
            document.add(new Paragraph("=== CLINIC APPOINTMENT REPORT ==="));
            document.add(new Paragraph(" ")); // Empty line

            document.add(new Paragraph("Doctor ID:" + app.getDoctor().getId()));
            document.add(new Paragraph(
                    "Doctor Name DR. " + app.getDoctor().getFirstName() + "" + app.getDoctor().getLastName()));
            document.add(new Paragraph("Specialization: " + app.getDoctor().getSpecialty()));
            document.add(new Paragraph(
                    "Patient Name : " + app.getPatient().getFirstName() + "" + app.getPatient().getLastName()));
            document.add(new Paragraph("Prescription Details: " + app.getPrescription().getPrescriptionNotes()));
            document.add(new Paragraph("=== Medications ==="));

            List<PrescriptionMedicationProjection> meds = prescriptionRepo
                    .getMedicationsForPrescription(app.getPrescription().getId());

            for (PrescriptionMedicationProjection med : meds) {
                String medLine = "- " + med.getMedicationName()
                        + " (Dosage: " + med.getDosage()
                        + ", Frequency: " + med.getFrequency() + ")";

                document.add(new Paragraph(medLine));
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error generating PDF", e);
        } finally {
            document.close();
        }

        return out.toByteArray();
    }

    @RabbitListener(queues = RabbitMQConstants.QUEUE_APPOINTMENT_COMPLETED)
    public void handleAppointmentCompletedEvent(int appointmentId) throws Exception {
        log.info("Message Received! Generating PDF for Appointment ID: " + appointmentId);

        Appointment app = appointmentRepo.findById(appointmentId).get();

        byte[] pdfData = generateAppointmentReport(app);

        Path desktopPath = Paths.get(System.getProperty("user.home"), "Desktop", "Report_" + appointmentId + ".pdf");
        Files.write(desktopPath, pdfData);

        log.info("Saved PDF to Desktop!");
    }

}
