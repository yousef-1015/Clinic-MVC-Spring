package com.example.clinicmvcspring.specifications;

import java.sql.Timestamp;

import org.springframework.data.jpa.domain.Specification;

import com.example.clinicmvcspring.models.Appointment;

public class AppointmentSpecification {

    public static Specification<Appointment> isBetweenDates(Timestamp start, Timestamp end) {
        return (root, query, cb) -> {
            if (start == null && end == null) {
                return cb.conjunction();
            }
            if (start != null && end == null) { // after
                return cb.greaterThanOrEqualTo(root.get("dateAndTime"), start);
            }
            if (start == null && end != null) { // before
                return cb.lessThanOrEqualTo(root.get("dateAndTime"), end);
            }
            return cb.between(root.get("dateAndTime"), start, end);

        };

    }
}
