package com.example.clinicmvcspring.repositories;

import java.util.List;
import java.util.Optional;

import javax.sql.DataSource;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.example.clinicmvcspring.models.Appointment;
import com.example.clinicmvcspring.models.AppointmentStatus;

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

    private final RowMapper<Appointment> rowMapper = (res, rowNum) -> new Appointment(
            res.getInt("id"),
            res.getTimestamp("date_and_time"),
            res.getInt("patient_id"),
            res.getInt("doctor_id"),
            AppointmentStatus.valueOf(res.getString("status")),
            res.getTimestamp("created_at"));

    // private final RowMapper<Appointment> rowMapper = new
    // BeanPropertyRowMapper<>(Appointment.class);
    // ! Wont work because the AppointmentStatus.valueOf(res.getString("status")),
    // needs to be done manually

    public int insert(Appointment app) {
        String sql = "INSERT INTO appointments (date_and_time, patient_id, doctor_id, status) " +
                "VALUES (:dateAndTime, :patientId, :doctorId, :status)";
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("dateAndTime", app.getDateAndTime())
                .addValue("patientId", app.getPatientId())
                .addValue("doctorId", app.getDoctorId())
                .addValue("status", app.getStatus());

        KeyHolder keyHolder = new GeneratedKeyHolder();
        namedJdbcTemplate.update(sql, params, keyHolder);
        return keyHolder.getKey().intValue();

    }

    public List<Appointment> findAll() {
        String sql = "SELECT * FROM appointments ORDER BY id";
        return jdbcTemplate.query(sql, rowMapper);
    }

    public List<Appointment> findAllPagination(int page, int size) {
        String sql = "SELECT * FROM appointments ORDER BY id LIMIT ? OFFSET ?";
        int offset = page * size;
        return jdbcTemplate.query(sql, rowMapper, size, offset);
    }

    public int count() {
        String sql = "SELECT COUNT(*) FROM appointments";
        return jdbcTemplate.queryForObject(sql, Integer.class);
    }

    public Optional<Appointment> findByID(int id) {
        String sql = "SELECT * FROM appointments WHERE id = ?";
        try {
            return Optional.of( jdbcTemplate.queryForObject(sql, rowMapper, id));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public int delete(Appointment app) {
        String sql = "DELETE FROM appointments WHERE id = ?";
        jdbcTemplate.update(sql, app.getId());
        return app.getId();
    }

    public int delete(int id) {
        String sql = "DELETE FROM appointments WHERE id = ?";
        jdbcTemplate.update(sql, id);
        return id;
    }

    public int update(int id, Appointment app) {
        String sql = "UPDATE appointments SET date_and_time = :dateAndTime, patient_id = :patientId, " +
                "doctor_id = :doctorId, status = :status WHERE id = :id";

        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("dateAndTime", app.getDateAndTime())
                .addValue("patientId", app.getPatientId())
                .addValue("doctorId", app.getDoctorId())
                .addValue("status", app.getStatus())
                .addValue("id", id);

        namedJdbcTemplate.update(sql, params);
        return id;
    }

}
