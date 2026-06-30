package com.example.clinicmvcspring.repositories;

import java.util.List;

import javax.sql.DataSource;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import com.example.clinicmvcspring.models.AppointmentModel;

@Repository
public class AppointmentRepo {
    private final DataSource dataSource;
    private final JdbcTemplate jdbcTemplate;
    private final NamedParameterJdbcTemplate namedJdbcTemplate;

    public AppointmentRepo(DataSource dataSource, JdbcTemplate jdbcTemplate,
            NamedParameterJdbcTemplate namedJdbcTemplate) {
        this.dataSource = dataSource;
        this.jdbcTemplate = jdbcTemplate;
        this.namedJdbcTemplate = namedJdbcTemplate;
    }

    private final RowMapper<AppointmentModel> rowMapper = (res, rowNum) -> new AppointmentModel(
            res.getInt("id"),
            res.getTimestamp("date_and_time"),
            res.getInt("patient_id"),
            res.getInt("doctor_id"),
            res.getString("status"),
            res.getTimestamp("created_at"));

    public boolean insertNewAppointment(AppointmentModel app) {
        String sql = "INSERT INTO appointments (date_and_time, patient_id, doctor_id, status) " +
                "VALUES (:dateAndTime, :patientId, :doctorId, :status)";
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("dateAndTime", app.getDateAndTime())
                .addValue("patientId", app.getPatientId())
                .addValue("doctorId", app.getDoctorId())
                .addValue("status", app.getStatus());
        int rowsAffected = namedJdbcTemplate.update(sql, params);
        return rowsAffected > 0;
    }

    public List<AppointmentModel> findAllAppointments() {
        String sql = "SELECT * FROM appointments";
        return jdbcTemplate.query(sql, rowMapper);
    }

    public AppointmentModel getAppointmentByID(int id) {
        String sql = "SELECT * FROM appointments WHERE id = ?";
        try {
            return jdbcTemplate.queryForObject(sql, rowMapper, id);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    public boolean deleteAppointmentFromDB(AppointmentModel app) {
        String sql = "DELETE FROM appointments WHERE id = ?";
        int rowsAffected = jdbcTemplate.update(sql, app.getId());
        return rowsAffected > 0;
    }

    public boolean deleteAppointmentFromDB(int id) {
        String sql = "DELETE FROM appointments WHERE id = ?";
        int rowsAffected = jdbcTemplate.update(sql, id);
        return rowsAffected > 0;
    }

}
