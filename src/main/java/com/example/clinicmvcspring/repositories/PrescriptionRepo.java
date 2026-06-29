package com.example.clinicmvcspring.repositories;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.stereotype.Repository;

import com.example.clinicmvcspring.models.PrescriptionModel;

@Repository
public class PrescriptionRepo {
 private DataSource dataSource;

    public PrescriptionRepo (DataSource dataSource)
    {
        this.dataSource =dataSource;}

    public boolean insertNewPrescription(PrescriptionModel pres) {
        String sql = "INSERT INTO prescriptions (prescription_notes, appointment_id) VALUES (?, ?)";

        try (Connection conn = dataSource.getConnection();
                PreparedStatement pr = conn.prepareStatement(sql)) {

            pr.setString(1, pres.getPrescriptionNotes());
            pr.setInt(2, pres.getAppointmentId());
            pr.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.out.println("Error saving prescription: " + e.getMessage());
            return false;
        }
    }

    public List<PrescriptionModel> findAllPrescriptions() {
        List<PrescriptionModel> allPrescriptions = new ArrayList<>();
        String sql = "SELECT * FROM prescriptions";

        try (Connection conn = dataSource.getConnection();
                PreparedStatement pr = conn.prepareStatement(sql);
                ResultSet res = pr.executeQuery()) {

            while (res.next()) {
                PrescriptionModel pres = new PrescriptionModel(
                        res.getInt("id"),
                        res.getString("prescription_notes"),
                        res.getInt("appointment_id"),
                        res.getTimestamp("created_at"));
                allPrescriptions.add(pres);
            }

        } catch (SQLException e) {
            System.out.println("Error loading prescriptions: " + e.getMessage());
        }
        return allPrescriptions;
    }

    public PrescriptionModel getPrescriptionByID(int id) {
        String sql = "SELECT * FROM prescriptions WHERE id = ?";

        try (Connection conn = dataSource.getConnection();
                PreparedStatement pr = conn.prepareStatement(sql)) {

            pr.setInt(1, id);
            ResultSet res = pr.executeQuery(); // Declared in body, closed with statement auto-close

            if (res.next()) {
                return new PrescriptionModel(
                        res.getInt("id"),
                        res.getString("prescription_notes"),
                        res.getInt("appointment_id"),
                        res.getTimestamp("created_at"));
            }

        } catch (SQLException e) {
            System.out.println("Error finding prescription: " + e.getMessage());
        }
        return null;
    }

    public boolean deletePrescriptionFromDB(PrescriptionModel pres) {
        String sql = "DELETE FROM prescriptions WHERE id = ?";

        try (Connection conn = dataSource.getConnection();
                PreparedStatement pr = conn.prepareStatement(sql)) {

            pr.setInt(1, pres.getId());
            pr.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.out.println("Couldn't delete prescription: " + e.getMessage());
            return false;
        }
    }

    public boolean deletePrescriptionFromDB(int id) {
        String sql = "DELETE FROM prescriptions WHERE id = ?";
        try (Connection conn = dataSource.getConnection();
                PreparedStatement pr = conn.prepareStatement(sql)) {

            pr.setInt(1, id);
            pr.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.out.println("Couldn't delete prescription: " + e.getMessage());
            return false;
        }
    }

}
