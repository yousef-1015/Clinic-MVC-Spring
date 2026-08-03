package com.example.clinicmvcspring.config;

public final class RabbitMQConstants {

    // EXCHANGES
    public static final String EXCHANGE_CLINIC_EVENTS = "clinic.events";
    public static final String EXCHANGE_DEAD_LETTER = "clinic.dlx";

    // QUEUES
    public static final String QUEUE_DOCTOR_CREATED = "doctor.created.queue";
    public static final String QUEUE_DOCTOR_UPDATED = "doctor.updated.queue";
    public static final String QUEUE_DOCTOR_DELETED = "doctor.deleted.queue";
    public static final String QUEUE_USER_LOGGEDIN = "user.loggedin.queue";
    public static final String QUEUE_DEAD_LETTER = "clinic.dlq";

    // ROUTING KEYS
    public static final String ROUTING_KEY_DOCTOR_CREATED = "doctor.created";
    public static final String ROUTING_KEY_DOCTOR_UPDATED = "doctor.updated";
    public static final String ROUTING_KEY_DOCTOR_DELETED = "doctor.deleted";
    public static final String ROUTING_KEY_USER_LOGGEDIN = "user.loggedin";
    public static final String ROUTING_KEY_DEAD_LETTER = "dead.letter";

    private RabbitMQConstants() {
    }

}
