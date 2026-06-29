package com.example.clinicmvcspring.repositories;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;
import org.springframework.stereotype.Repository;

import com.example.clinicmvcspring.models.AppointmentModel;

@Repository
public class AppointmentRepo {
    private final DataSource dataSource;

    public AppointmentRepo(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public boolean insertNewAppointment(AppointmentModel app) {
        String sql = "INSERT INTO appointments (date_and_time, patient_id, doctor_id, status) VALUES (?, ?, ?, ?)";

        try (Connection conn = dataSource.getConnection();
                PreparedStatement pr = conn.prepareStatement(sql)) {

            pr.setTimestamp(1, app.getDateAndTime());
            pr.setInt(2, app.getPatientId());
            pr.setInt(3, app.getDoctorId());
            pr.setString(4, app.getStatus());
            pr.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.out.println("Error saving appointment: " + e.getMessage());
            return false;
        }
    }

    public List<AppointmentModel> findAllAppointments() {
        List<AppointmentModel> allAppointments = new ArrayList<>();
        String sql = "SELECT * FROM appointments";

        try (Connection conn = dataSource.getConnection();
                PreparedStatement pr = conn.prepareStatement(sql);
                ResultSet res = pr.executeQuery()) {

            while (res.next()) {
                AppointmentModel app = new AppointmentModel(
                        res.getInt("id"),
                        res.getTimestamp("date_and_time"),
                        res.getInt("patient_id"),
                        res.getInt("doctor_id"),
                        res.getString("status"),
                        res.getTimestamp("created_at"));
                allAppointments.add(app);
            }

        } catch (SQLException e) {
            System.out.println("Error loading appointments: " + e.getMessage());
        }
        return allAppointments;
    }

    public AppointmentModel getAppointmentByID(int id) {
        String sql = "SELECT * FROM appointments WHERE id = ?";

        try (Connection conn = dataSource.getConnection();
                PreparedStatement pr = conn.prepareStatement(sql)) {

            pr.setInt(1, id);
            ResultSet res = pr.executeQuery();

            if (res.next()) {
                return new AppointmentModel(
                        res.getInt("id"),
                        res.getTimestamp("date_and_time"),
                        res.getInt("patient_id"),
                        res.getInt("doctor_id"),
                        res.getString("status"),
                        res.getTimestamp("created_at"));
            }

        } catch (SQLException e) {
            System.out.println("Error finding appointment: " + e.getMessage());
        }
        return null;
    }

    public boolean deleteAppointmentFromDB(AppointmentModel app) {
        String sql = "DELETE FROM appointments WHERE id = ?";

        try (Connection conn = dataSource.getConnection();
                PreparedStatement pr = conn.prepareStatement(sql)) {

            pr.setInt(1, app.getId());
            pr.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.out.println("Couldn't delete appointment: " + e.getMessage());
            return false;
        }
    }

    public boolean deleteAppointmentFromDB(int id) {
        String sql = "DELETE FROM appointments WHERE id = ?";
        try (Connection conn = dataSource.getConnection();
                PreparedStatement pr = conn.prepareStatement(sql)) {

            pr.setInt(1, id);
            pr.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.out.println("Couldn't delete appointment: " + e.getMessage());
            return false;
        }
    }

}
