# Clinic-MVC-Spring

A Spring Boot REST API for clinic management — covering doctors, patients, appointments, prescriptions, and medications, secured with JWT and backed by RabbitMQ, Kafka, and Redis.

![Java](https://img.shields.io/badge/Java-25-blue?logo=openjdk)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-4.1.0-brightgreen?logo=springboot)
![MySQL](https://img.shields.io/badge/MySQL-8.0-orange?logo=mysql)
![Maven](https://img.shields.io/badge/Build-Maven-red?logo=apachemaven)
![Docker](https://img.shields.io/badge/Docker-Compose-blue?logo=docker)
![License](https://img.shields.io/badge/License-Unlicensed-lightgrey)

---

## Tech Stack

| Layer | Technology |
|---|---|
| Language | Java 25 |
| Framework | Spring Boot 4.1.0 |
| Persistence | Spring Data JPA + Hibernate + HikariCP |
| Database | MySQL 8.0 |
| Security | Spring Security + JJWT 0.11.5 |
| Caching | Redis (Spring Cache) |
| Messaging | RabbitMQ (AMQP) + Apache Kafka (KRaft) |
| AOP | Spring AOP — audit logging + execution timing |
| DTO Mapping | MapStruct 1.5.5 |
| API Docs | SpringDoc OpenAPI 3 (Swagger UI) |
| Build | Maven (wrapper included) |
| Containers | Docker + Docker Compose |
| Testing | JUnit 5, Spring Boot Test, H2 (test profile) |

---

## Key Features

- Stateless JWT authentication with refresh token rotation and logout blacklisting
- Role-based access control — `ADMIN`, `DOCTOR`, `ACCOUNTANT`
- Paginated responses on all list endpoints (`?page=0&size=5`)
- Dynamic filtering: doctors by specialty/salary, appointments by status and date range
- Doctor lifecycle events published to RabbitMQ; audit and login events streamed to Kafka
- AOP audit logging via `@Audit` — every annotated service call writes an `AuditLog` row
- Full Swagger UI at `/swagger-ui.html`
- One-command Docker Compose stack (app + MySQL + Redis + RabbitMQ + Kafka)

---

## Prerequisites

| Requirement | Notes |
|---|---|
| Java 25 | Required to build and run |
| Docker + Docker Compose | Required for the recommended setup |
| MySQL 8.0 | Only if running without Docker |
| Redis | Only if running without Docker |

---

## Installation & Setup

### Option A — Docker Compose (Recommended)

```bash
git clone https://github.com/yousef-1015/Clinic-MVC-Spring.git
cd Clinic-MVC-Spring
docker compose up --build
```

The `.env` file in the repo root sets all credentials:

```env
MYSQL_ROOT_PASSWORD=1234
MYSQL_DATABASE=hospitaldb
SPRING_DATASOURCE_USERNAME=root
SPRING_DATASOURCE_PASSWORD=1234
RABBITMQ_USERNAME=root
RABBITMQ_PASSWORD=1234
```

To seed sample data, wait ~30 s for MySQL to be healthy, then:

```bash
docker exec -i clinic-mvc-spring-mysql-1 mysql -uroot -p1234 hospitaldb < Hostpital_db.sql
```

Services started by Compose:

| Service | Host Port |
|---|---|
| Spring Boot API | 8080 |
| MySQL 8.0 | 3307 |
| Redis | 6380 |
| RabbitMQ (AMQP / Management UI) | 5672 / 15672 |
| Kafka | 9092 |

---

### Option B — Local Development

```bash
git clone https://github.com/yousef-1015/Clinic-MVC-Spring.git
cd Clinic-MVC-Spring

# Import schema
mysql -u root -p hospitaldb < Hostpital_db.sql

# Build (skipping tests)
./mvnw clean package -DskipTests   # Linux/macOS
mvnw.cmd clean package -DskipTests # Windows
```

Edit `src/main/resources/application.properties` to point to your local MySQL, Redis, RabbitMQ, and Kafka instances.

---

## Running the Application

```bash
# Docker Compose
docker compose up

# Maven dev server
./mvnw spring-boot:run

# Prebuilt JAR
java -jar target/Clinic-MVC-Spring-0.0.1-SNAPSHOT.jar
```

| Endpoint | URL |
|---|---|
| API base | `http://localhost:8080/api/v1` |
| Swagger UI | `http://localhost:8080/swagger-ui.html` |
| OpenAPI JSON | `http://localhost:8080/v3/api-docs` |
| RabbitMQ UI | `http://localhost:15672` (root / 1234) |

---

## API Endpoints

All endpoints except `login`, `logout`, `refresh`, and Swagger paths require a Bearer JWT token.

```
Authorization: Bearer <access-token>
```

### Authentication — `/api/v1/users`

| Method | Path | Role |
|---|---|---|
| POST | `/api/v1/users/signup` | Public |
| POST | `/api/v1/users/login` | Public |
| POST | `/api/v1/users/logout` | Public |
| POST | `/api/v1/users/refresh` | Public |
| GET  | `/api/v1/users?page=0&size=5` | ADMIN |

**Login:**
```json
{ "username": "admin", "password": "yourpassword" }
```
**Signup** (link to a doctor profile via optional `foreignId`):
```json
{ "username": "dr_smith", "password": "securepass", "role": "DOCTOR", "foreignId": 5 }
```

---

### Doctors — `/api/v1/doctors` (ADMIN only)

| Method | Path | Description |
|---|---|---|
| GET | `/api/v1/doctors?page=0&size=5` | All doctors, paginated |
| GET | `/api/v1/doctors/{id}` | Single doctor |
| POST | `/api/v1/doctors` | Create |
| PUT | `/api/v1/doctors/{id}` | Full update |
| PATCH | `/api/v1/doctors/{id}` | Partial update |
| DELETE | `/api/v1/doctors/{id}` | Delete |
| GET | `/api/v1/doctors/specialty?specialty=Cardiology` | Filter by specialty |
| GET | `/api/v1/doctors/search?specialty=Cardiology&salary=9000` | Dynamic filter |

```json
{ "firstName": "John", "lastName": "Smith", "email": "john@clinic.com", "specialty": "Cardiology", "salary": 12000.00 }
```

---

### Patients — `/api/v1/patients` (ADMIN only)

| Method | Path | Description |
|---|---|---|
| GET | `/api/v1/patients?page=0&size=5` | All patients, paginated |
| GET | `/api/v1/patients/{id}` | Single patient |
| POST | `/api/v1/patients` | Create |
| PUT | `/api/v1/patients/{id}` | Full update |
| PATCH | `/api/v1/patients/{id}` | Partial update |
| DELETE | `/api/v1/patients/{id}` | Delete |

```json
{ "firstName": "Ahmad", "lastName": "Mansour", "email": "ahmad@email.com" }
```

---

### Appointments — `/api/v1/appointments` (ADMIN, DOCTOR)

| Method | Path | Description |
|---|---|---|
| GET | `/api/v1/appointments?page=0&size=5` | All appointments, paginated |
| GET | `/api/v1/appointments/{id}` | Single appointment |
| POST | `/api/v1/appointments` | Create |
| PUT | `/api/v1/appointments/{id}` | Full update |
| PATCH | `/api/v1/appointments/{id}` | Partial update |
| DELETE | `/api/v1/appointments/{id}` | Delete |
| GET | `/api/v1/appointments/status?status=SCHEDULED` | Filter by status |
| GET | `/api/v1/appointments/search/date?start=...&end=...` | Filter by date range |

```json
{ "dateAndTime": "2026-09-15 10:00:00", "patientId": 1, "doctorId": 2, "status": "SCHEDULED" }
```

---

### Medications — `/api/v1/medications` (ADMIN only)

| Method | Path | Description |
|---|---|---|
| GET | `/api/v1/medications?page=0&size=5` | All medications, paginated |
| GET | `/api/v1/medications/{id}` | Single medication |
| POST | `/api/v1/medications` | Create |
| PUT | `/api/v1/medications/{id}` | Full update |
| PATCH | `/api/v1/medications/{id}` | Partial update |
| DELETE | `/api/v1/medications/{id}` | Delete |

```json
{ "medicationName": "Panadol" }
```

---

### Prescriptions — `/api/v1/prescriptions` (ADMIN, DOCTOR)

| Method | Path | Description |
|---|---|---|
| GET | `/api/v1/prescriptions?page=0&size=5` | All prescriptions with medications, paginated |
| GET | `/api/v1/prescriptions/{id}` | Single prescription |
| POST | `/api/v1/prescriptions` | Create with medications |
| PUT | `/api/v1/prescriptions/{id}` | Full update |
| PATCH | `/api/v1/prescriptions/{id}` | Partial update |
| DELETE | `/api/v1/prescriptions/{id}` | Delete |

```json
{
  "prescriptionNotes": "Take after meals.",
  "appointmentId": 4,
  "medications": [
    { "medicationId": 1, "dosage": "500mg", "frequency": "Twice daily for 7 days" }
  ]
}
```

---

## Database Schema

**Relationships at a glance:**
- `Doctor` optionally links to one `User`
- `Appointment` belongs to one `Patient` (many-to-one) and one `Doctor` (many-to-one)
- `Prescription` maps one-to-one to an `Appointment` (unique)
- `Prescription` has many `Medications` through `prescription_medications` (dosage + frequency per entry)

| Table | Key Columns |
|---|---|
| `doctors` | id, first_name, last_name, email (unique), salary, hire_date, specialty, user_id (FK) |
| `patients` | id, first_name, last_name, email (unique), created_at |
| `appointments` | id, date_and_time, patient_id (FK), doctor_id (FK), status (enum), created_at |
| `prescriptions` | id, prescription_notes, appointment_id (FK unique), created_at |
| `medications` | id, medication_name, created_at |
| `prescription_medications` | prescription_id + medication_id (composite PK), dosage, frequency |
| `users` | id, username (unique), password (BCrypt), role (enum), enabled |
| `refresh_tokens` | id, token (unique), user_id (FK), expiry_date |
| `audit_logs` | id, action_type, made_by, performed_at, details |

> Appointments enforce no-double-booking via unique constraints on `(doctor_id, date_and_time)` and `(patient_id, date_and_time)`.

---

## Testing

Tests use H2 in-memory database — no external services required.

```bash
./mvnw test          # Linux/macOS
mvnw.cmd test        # Windows
```

| Test File | Coverage |
|---|---|
| `ClinicMvcSpringApplicationTests` | Context load check |
| `DoctorControllerTest` | Controller layer (MockMvc) |
| `UserControllerTest` | Auth endpoints (unit) |
| `UserControllerIntegrationTest` | Full signup/login/logout flow |
| `RefreshTokenRepoTest` | Repository layer (`@DataJpaTest`) |
| `JwtServiceTest` | Token generation and validation |
| `AppUserDetailsServiceTest` | UserDetailsService loading |
| `RefreshTokenServiceTest` | Refresh token lifecycle |

---

## Contributing

1. Fork the repository
2. Create a feature branch: `git checkout -b feature/your-feature`
3. Commit your changes and push
4. Open a Pull Request

Keep controllers thin, put business logic in services, use DTOs for all API shapes, and cover new functionality with tests.

---

## License

Unlicensed — all rights reserved by the author.
