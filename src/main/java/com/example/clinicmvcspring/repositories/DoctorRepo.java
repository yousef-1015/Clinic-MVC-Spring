package com.example.clinicmvcspring.repositories;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.stereotype.Repository;

import com.example.clinicmvcspring.models.DoctorModel;

@Repository
public class DoctorRepo {
    private DataSource dataSource;

    public DoctorRepo(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public boolean insertANewDoctor(DoctorModel doc) {

        String sql = "INSERT INTO doctors (first_name, last_name, email, salary, specialty) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = dataSource.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, doc.getFirstName());
            pstmt.setString(2, doc.getLastName());
            pstmt.setString(3, doc.getEmail());
            pstmt.setDouble(4, doc.getSalary());
            pstmt.setString(5, doc.getSpecialty());

            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Error saving doctor: " + e.getMessage());
            return false;
        }
    }

    public List<DoctorModel> findAllDoctors() {
        List<DoctorModel> allDoctors = new ArrayList<>();
        String sql = "SELECT * FROM doctors";
        try (Connection conn = dataSource.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql);
                ResultSet res = pstmt.executeQuery()) {
            while (res.next()) {
                DoctorModel doc = new DoctorModel(
                        res.getInt("id"),
                        res.getString("first_name"),
                        res.getString("last_name"),
                        res.getString("email"),
                        res.getDouble("salary"),
                        res.getDate("hire_date"),
                        res.getString("specialty"));
                allDoctors.add(doc);
            }
        } catch (SQLException e) {
            System.out.println("Error loading doctors: " + e.getMessage());
        }
        return allDoctors;
    }

    public boolean deleteDoctorFromDB(DoctorModel docToDelete) {
        String stmt = "DELETE FROM doctors WHERE id = ?";
        try (Connection conn = dataSource.getConnection();
                PreparedStatement pr = conn.prepareStatement(stmt)) {
            pr.setInt(1, docToDelete.getId());
            pr.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.print("Couldn't Delete doctor from db: " + e.getMessage());
            return false;
        }
    }

    public DoctorModel getDoctorByID(int id) {
        String sql = "SELECT * FROM doctors WHERE id = ?";

        try (Connection conn = dataSource.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            ResultSet res = pstmt.executeQuery();

            if (res.next()) {
                return new DoctorModel(
                        res.getInt("id"),
                        res.getString("first_name"),
                        res.getString("last_name"),
                        res.getString("email"),
                        res.getDouble("salary"),
                        res.getDate("hire_date"),
                        res.getString("specialty"));
            }
        } catch (SQLException e) {
            System.out.println("Error finding doctor: " + e.getMessage());
        }

        return null;
    }

    public boolean deleteDoctorFromDB(int id) {
        String stmt = "DELETE FROM doctors WHERE id = ?";
        try (Connection conn = dataSource.getConnection();
                PreparedStatement pr = conn.prepareStatement(stmt)) {

            pr.setInt(1, id);
            pr.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.out.print("Couldn't Delete doctor from db: " + e.getMessage());
            return false;
        }
    }

    public boolean updateDoctorInDB(DoctorModel doc) {
        String sql = "UPDATE doctors SET first_name = ?, last_name = ?, email = ?, salary = ?, specialty = ? WHERE id = ?";
        try (Connection conn = dataSource.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, doc.getFirstName());
            pstmt.setString(2, doc.getLastName());
            pstmt.setString(3, doc.getEmail());
            pstmt.setDouble(4, doc.getSalary());
            pstmt.setString(5, doc.getSpecialty());
            pstmt.setInt(6, doc.getId());
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Error updating doctor: " + e.getMessage());
            return false;
        }
    }

}