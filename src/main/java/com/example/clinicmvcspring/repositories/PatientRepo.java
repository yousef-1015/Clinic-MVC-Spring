package com.example.clinicmvcspring.repositories;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.stereotype.Repository;

import com.example.clinicmvcspring.models.PatientModel;

@Repository
public class PatientRepo {
    private DataSource dataSource;

    public PatientRepo(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public boolean insertNewPatient(PatientModel newPatient) {
        String query = "INSERT INTO patients (first_name, last_name, email) VALUES (?, ?, ?)";
        try (Connection conn = dataSource.getConnection();
                PreparedStatement pr = conn.prepareStatement(query)) {
            pr.setString(1, newPatient.getFirstName());
            pr.setString(2, newPatient.getLastName());
            pr.setString(3, newPatient.getEmail());
            pr.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Error saving patient: " + e.getMessage());
            return false;
        }
    }

    public List<PatientModel> getAllPatients() {
        List<PatientModel> allPatients = new ArrayList<>();
        String sqlStmt = "SELECT * FROM patients";
        try (Connection conn = dataSource.getConnection();
                PreparedStatement prStmt = conn.prepareStatement(sqlStmt);
                ResultSet res = prStmt.executeQuery()) {
            while (res.next()) {
                PatientModel pat = new PatientModel(
                        res.getInt("id"),
                        res.getString("first_name"),
                        res.getString("last_name"),
                        res.getString("email"),
                        res.getTimestamp("created_at"));
                allPatients.add(pat);
            }
        } catch (SQLException e) {
            System.out.print("error loading patients : " + e.getMessage());
        }
        return allPatients;
    }

    public boolean deletePatientFromDB(PatientModel pat) {
        String sql = "DELETE FROM patients WHERE id = ?";
        try (Connection conn = dataSource.getConnection();
                PreparedStatement pr = conn.prepareStatement(sql)) {
            pr.setInt(1, pat.getId());
            pr.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Couldn't delete patient: " + e.getMessage());
            return false;
        }
    }

    public PatientModel getPatientByID(int id) {
        String sql = "SELECT * FROM patients WHERE id = ?";
        try (Connection conn = dataSource.getConnection();
                PreparedStatement pr = conn.prepareStatement(sql)) {
            pr.setInt(1, id);
            ResultSet rs = pr.executeQuery();
            if (rs.next()) {
                return new PatientModel(
                        rs.getInt("id"),
                        rs.getString("first_name"),
                        rs.getString("last_name"),
                        rs.getString("email"),
                        rs.getTimestamp("created_at"));

            }
        } catch (SQLException e) {
            System.out.println("Error finding patient: " + e.getMessage());
        }
        return null;
    }

    public boolean deletePatientFromDB(int id) {
        String sql = "DELETE FROM patients WHERE id = ?";
        try (Connection conn = dataSource.getConnection();
                PreparedStatement pr = conn.prepareStatement(sql)) {

            pr.setInt(1, id);
            pr.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.out.println("Couldn't delete patient: " + e.getMessage());
            return false;
        }
    }

}
