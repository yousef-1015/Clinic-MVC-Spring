package com.example.clinicmvcspring.repositories;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.stereotype.Repository;

import com.example.clinicmvcspring.models.MedicationModel;
@Repository
public class MedicationRepo {
    private DataSource dataSource;

    public MedicationRepo (DataSource dataSource)
    {
        this.dataSource = dataSource;
    }

    public boolean insertNewMedication(MedicationModel med) {
        String sql = "INSERT INTO medications (medication_name) VALUES (?)";

        try (Connection conn = dataSource.getConnection();
                PreparedStatement pr = conn.prepareStatement(sql)) {

            pr.setString(1, med.getMedicationName());
            pr.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.out.println("Error saving medication: " + e.getMessage());
            return false;
        }
    }

    public List<MedicationModel> findAllMedications() {
        List<MedicationModel> allMedications = new ArrayList<>();
        String sql = "SELECT * FROM medications";

        try (Connection conn = dataSource.getConnection();
                PreparedStatement pr = conn.prepareStatement(sql);
                ResultSet res = pr.executeQuery()) {

            while (res.next()) {
                MedicationModel med = new MedicationModel(
                        res.getInt("id"),
                        res.getString("medication_name"),
                        res.getTimestamp("created_at"));
                allMedications.add(med);
            }

        } catch (SQLException e) {
            System.out.println("Error loading medications: " + e.getMessage());
        }
        return allMedications;
    }

    public MedicationModel getMedicationByID(int id) {
        String sql = "SELECT * FROM medications WHERE id = ?";

        try (Connection conn = dataSource.getConnection();
                PreparedStatement pr = conn.prepareStatement(sql)) {

            pr.setInt(1, id);
            ResultSet res = pr.executeQuery();

            if (res.next()) {
                return new MedicationModel(
                        res.getInt("id"),
                        res.getString("medication_name"),
                        res.getTimestamp("created_at"));
            }

        } catch (SQLException e) {
            System.out.println("Error finding medication: " + e.getMessage());
        }
        return null;
    }

    public boolean deleteMedicationFromDB(MedicationModel med) {
        String sql = "DELETE FROM medications WHERE id = ?";

        try (Connection conn = dataSource.getConnection();
                PreparedStatement pr = conn.prepareStatement(sql)) {

            pr.setInt(1, med.getId());
            pr.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.out.println("Couldn't delete medication: " + e.getMessage());
            return false;
        }
    }

    public boolean deleteMedicationFromDB(int id) {
        String sql = "DELETE FROM medications WHERE id = ?";
        try (Connection conn = dataSource.getConnection();
                PreparedStatement pr = conn.prepareStatement(sql)) {

            pr.setInt(1, id);
            pr.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.out.println("Couldn't delete medication: " + e.getMessage());
            return false;
        }
    }

}
