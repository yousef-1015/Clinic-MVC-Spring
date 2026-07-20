CREATE DATABASE  IF NOT EXISTS `hospitaldb` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `hospitaldb`;
-- MySQL dump 10.13  Distrib 8.0.46, for Win64 (x86_64)
--
-- Host: localhost    Database: hospitaldb
-- ------------------------------------------------------
-- Server version	8.0.46

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `appointments`
--

DROP TABLE IF EXISTS `appointments`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `appointments` (
  `id` int NOT NULL AUTO_INCREMENT,
  `date_and_time` datetime NOT NULL,
  `patient_id` int DEFAULT NULL,
  `doctor_id` int DEFAULT NULL,
  `status` enum('Scheduled','Completed','Cancelled') NOT NULL DEFAULT 'Scheduled',
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `unique_doctor_schedule` (`doctor_id`,`date_and_time`),
  UNIQUE KEY `unique_patient_schedule` (`patient_id`,`date_and_time`),
  KEY `idx_appointment_date` (`date_and_time`),
  KEY `idx_appt_doc_pat` (`doctor_id`,`patient_id`),
  CONSTRAINT `fk_appointment_doctor` FOREIGN KEY (`doctor_id`) REFERENCES `doctors` (`id`) ON DELETE RESTRICT,
  CONSTRAINT `fk_appointment_patient` FOREIGN KEY (`patient_id`) REFERENCES `patients` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `appointments`
--

LOCK TABLES `appointments` WRITE;
/*!40000 ALTER TABLE `appointments` DISABLE KEYS */;
INSERT INTO `appointments` VALUES (1,'2026-06-19 10:00:00',1,1,'Completed','2026-06-18 13:00:00'),(2,'2026-06-20 11:30:00',2,2,'Completed','2026-06-18 13:30:00'),(4,'2026-06-23 14:00:00',4,3,'Scheduled','2026-06-19 06:00:00'),(7,'2024-10-12 11:00:00',7,1,'Completed','2024-10-12 06:00:00'),(8,'2026-06-25 10:00:00',1,1,'Scheduled','2026-06-21 12:37:01'),(10,'2026-06-25 12:00:00',7,1,'Scheduled','2026-06-21 12:37:01'),(11,'2026-07-01 10:00:00',2,2,'Scheduled','2026-06-23 08:10:02'),(12,'2026-07-20 10:00:00',1,2,'Scheduled','2026-07-06 10:36:07');
/*!40000 ALTER TABLE `appointments` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `audit_logs`
--

DROP TABLE IF EXISTS `audit_logs`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `audit_logs` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `action_type` varchar(50) NOT NULL,
  `made_by` varchar(100) NOT NULL,
  `performed_at` datetime NOT NULL,
  `details` varchar(500) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `audit_logs`
--

LOCK TABLES `audit_logs` WRITE;
/*!40000 ALTER TABLE `audit_logs` DISABLE KEYS */;
/*!40000 ALTER TABLE `audit_logs` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `doctors`
--

DROP TABLE IF EXISTS `doctors`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `doctors` (
  `id` int NOT NULL AUTO_INCREMENT,
  `first_name` varchar(50) NOT NULL,
  `last_name` varchar(50) NOT NULL,
  `email` varchar(100) NOT NULL,
  `salary` decimal(6,2) NOT NULL,
  `hire_date` date NOT NULL DEFAULT (curdate()),
  `specialty` varchar(50) NOT NULL,
  `user_id` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `email` (`email`),
  KEY `idx_doctor_specialty` (`specialty`),
  KEY `fk_doctor_user` (`user_id`),
  CONSTRAINT `fk_doctor_user` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE SET NULL
) ENGINE=InnoDB AUTO_INCREMENT=27 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `doctors`
--

LOCK TABLES `doctors` WRITE;
/*!40000 ALTER TABLE `doctors` DISABLE KEYS */;
INSERT INTO `doctors` VALUES (1,'Tarek','Ziad','tarek.newemail@email.com',999.50,'2022-01-15','Cardiology',NULL),(2,'Rami','Odeh','rami@email.com',920.00,'2023-06-10','Pediatrics',NULL),(3,'Sami','Zaid','sami@email.com',780.25,'2024-03-20','Cardiology',NULL),(4,'Ali','Mansour','Ali.mansour@hospital.com',4500.00,'2026-06-21','Pediatrics',NULL),(6,'raed','malek','raed.malek@clinic.com',8500.50,'2026-06-22','Cardiology',NULL),(9,'moath','malek','moath.malek@clinic.com',9500.50,'2026-06-23','Cardiology',NULL),(18,'khaled','moha','khaleed@gmail.com',6004.00,'2026-06-25','Cardiology',NULL),(20,'khaled','mohammad','khaleeed@gmail.com',6004.00,'2026-06-25','Cardiology',NULL),(24,'mohannad','hussien','huss@gmail.com',6004.00,'2026-06-25','Cardiology',NULL),(26,'Maher','samer','maher.samer@gmail.com',9050.00,'2026-06-30','Cardiology',NULL);
/*!40000 ALTER TABLE `doctors` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `medications`
--

DROP TABLE IF EXISTS `medications`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `medications` (
  `id` int NOT NULL AUTO_INCREMENT,
  `medication_name` varchar(100) NOT NULL,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `medications`
--

LOCK TABLES `medications` WRITE;
/*!40000 ALTER TABLE `medications` DISABLE KEYS */;
INSERT INTO `medications` VALUES (1,'Amoxicillin','2026-06-18 09:00:00'),(2,'Ibuprofen','2026-06-18 09:00:00'),(3,'Lipitor','2026-06-18 09:00:00'),(5,'Paracetamol','2026-06-23 08:36:31');
/*!40000 ALTER TABLE `medications` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `patients`
--

DROP TABLE IF EXISTS `patients`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `patients` (
  `id` int NOT NULL AUTO_INCREMENT,
  `first_name` varchar(50) NOT NULL,
  `last_name` varchar(50) NOT NULL,
  `email` varchar(100) NOT NULL,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `email` (`email`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `patients`
--

LOCK TABLES `patients` WRITE;
/*!40000 ALTER TABLE `patients` DISABLE KEYS */;
INSERT INTO `patients` VALUES (1,'Ahmad','Mansour','ahmad@email.com','2026-06-18 12:59:02'),(2,'Mohammad','Ali','mohammad@email.com','2026-06-18 12:59:02'),(3,'Omar','Hassan','omar@email.com','2026-06-18 12:59:02'),(4,'Emad','Mohammd','emad@email.com','2026-06-18 12:59:02'),(5,'Khaled','Sadiq','khaled@email.com','2026-06-18 12:59:02'),(7,'Sami','rami','sami@email.com','2024-09-01 06:00:00'),(8,'zaid','yazan','zaid.yazan@email.com','2026-06-22 12:32:37'),(9,'sara','ahmad','sara.ahmad@email.com','2026-06-23 07:42:47');
/*!40000 ALTER TABLE `patients` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `prescription_medications`
--

DROP TABLE IF EXISTS `prescription_medications`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `prescription_medications` (
  `prescription_id` int NOT NULL,
  `medication_id` int NOT NULL,
  `dosage` varchar(50) NOT NULL,
  `frequency` varchar(100) NOT NULL,
  PRIMARY KEY (`prescription_id`,`medication_id`),
  KEY `medication_id` (`medication_id`),
  CONSTRAINT `prescription_medications_ibfk_1` FOREIGN KEY (`prescription_id`) REFERENCES `prescriptions` (`id`),
  CONSTRAINT `prescription_medications_ibfk_2` FOREIGN KEY (`medication_id`) REFERENCES `medications` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `prescription_medications`
--

LOCK TABLES `prescription_medications` WRITE;
/*!40000 ALTER TABLE `prescription_medications` DISABLE KEYS */;
INSERT INTO `prescription_medications` VALUES (1,3,'10mg','Once daily at bedtime'),(2,1,'500mg','Three times daily for 7 days'),(2,2,'400mg','Every 6 hours as needed for pain'),(13,1,'500mg','Once daily'),(13,2,'400mg','Twice daily'),(15,3,'10mg','Once daily at bedtime');
/*!40000 ALTER TABLE `prescription_medications` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `prescriptions`
--

DROP TABLE IF EXISTS `prescriptions`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `prescriptions` (
  `id` int NOT NULL AUTO_INCREMENT,
  `prescription_notes` varchar(200) DEFAULT NULL,
  `appointment_id` int DEFAULT NULL,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `unique_appointment` (`appointment_id`),
  CONSTRAINT `fk_prescription_appointment` FOREIGN KEY (`appointment_id`) REFERENCES `appointments` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `prescriptions`
--

LOCK TABLES `prescriptions` WRITE;
/*!40000 ALTER TABLE `prescriptions` DISABLE KEYS */;
INSERT INTO `prescriptions` VALUES (1,'Take after meals. Follow up in two weeks.',1,'2026-06-19 07:45:00'),(2,'Keep hydrated. Complete the full course.',2,'2026-06-20 09:00:00'),(5,'Take after meals. Rest for 3 days.',4,'2026-06-23 08:54:57'),(12,'Testing many-to-many database crash',8,'2026-07-05 09:18:23'),(13,'Take one tablet of medicine every morning.',10,'2026-07-06 07:35:15'),(15,'Rest for 3 days and drink plenty of water.',11,'2026-07-06 10:39:23');
/*!40000 ALTER TABLE `prescriptions` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `refresh_tokens`
--

DROP TABLE IF EXISTS `refresh_tokens`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `refresh_tokens` (
  `id` int NOT NULL AUTO_INCREMENT,
  `token` varchar(255) NOT NULL,
  `user_id` int DEFAULT NULL,
  `expiry_date` datetime NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `token` (`token`),
  KEY `FK1lih5y2npsf8u5o3vhdb9y0os` (`user_id`),
  CONSTRAINT `FK1lih5y2npsf8u5o3vhdb9y0os` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=85 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `refresh_tokens`
--

LOCK TABLES `refresh_tokens` WRITE;
/*!40000 ALTER TABLE `refresh_tokens` DISABLE KEYS */;
INSERT INTO `refresh_tokens` VALUES (6,'7ad4fcaf-35d4-4f36-be3a-c2375b2f3fc0',2,'2026-07-20 13:52:58'),(45,'p4n5vBFgy4eqSSa0XpRBh2+/t6Pv/mlhbOBkUzUl/MQ=',1,'2026-07-26 11:54:35'),(46,'aRf5UbKBRQkikWAmo41Vj+Y9NgwFHyX62xVEOj4qOgU=',2,'2026-07-26 11:54:57'),(47,'TSGUpgGVacxr/m7JaQEkcfgYnKzJ7qXSPBty+Id9WHg=',2,'2026-07-26 12:00:28'),(48,'2xf25xHJ464UwYm5C177az7WaiT2ICeyv1rULu8nL8I=',2,'2026-07-26 12:13:43'),(49,'GUI+V0gIQWpBMz5CyXX2Ev0Pm+qpfz60bq0KqIM5zAw=',2,'2026-07-26 12:14:42'),(50,'x2i8FP18KdOS3tG2MwfZ/2PUwxF4+iqh9FeOgqDlyWw=',2,'2026-07-26 12:17:17'),(51,'leDaWwbbhZpgR6z1impPpaXrr31skPMZsHXvkOwTaLQ=',2,'2026-07-26 12:18:43'),(52,'ANU718mE8W0Ta5h/Rz+EnBeuHU9XvkoH8BTi0fqLxss=',2,'2026-07-26 12:23:48'),(53,'9KFUHn/ETNHNSr7zTwl7pe0kxFE/nZwe3l8lpz4vQwM=',1,'2026-07-26 12:25:43'),(54,'xyNKhf8fMcierUcYbwr6ArVm74QJe46+gx0H4Njcg3U=',2,'2026-07-26 12:31:49'),(55,'e212mv7+ontFsysJ1WVp12bz63FbuYX5nhRtJQUKIP0=',2,'2026-07-26 12:45:00'),(56,'4EmL/EGh0QkJRTE3UvNn4vA6Qhz2m73rJNwS9xQ/bts=',2,'2026-07-27 07:23:39'),(57,'fWCKDAFT2C3V8Lu3Ck1j77O4Fu8YOxhicw1AjW3Vto4=',2,'2026-07-27 07:24:39'),(58,'Ub6/6uskucATeh26phq7QDEbSsTw2jG4xB0KYzk7Fi4=',2,'2026-07-27 07:30:21'),(59,'InUJ3h3WQ7TDoVjEkCgXAWcegkMNtQBx1qT8i7z7SMQ=',2,'2026-07-27 07:34:56'),(60,'ojeBky61TdxJqmkGoeDiBDwsRKK7gnidXC5vi3zkclw=',2,'2026-07-27 07:43:50'),(61,'GTcYbw2dTf2RaIpPhCo5I6gnxN0FbKQWhmms1SIIfJM=',2,'2026-07-27 07:48:48'),(62,'qw9vY6sXuTUdz3PCcv4cAfcvbtNS/PDAPXJWhdBDJwE=',2,'2026-07-27 07:49:35'),(63,'D0Qzy62zxowjqBxAb1kd9WJdJW+hkoNK7sfKi/0KXf8=',2,'2026-07-27 07:53:34'),(64,'GMVQw9q/7/2F3iOyqixmo8AtI8/sdCw5eQshE1ITTYs=',2,'2026-07-27 07:54:18'),(65,'N6zP+wje/2I6JL+lEWUxOeUAllBVSA0yUJGQfLudjtQ=',2,'2026-07-27 07:55:35'),(66,'AyQCj5SGdqSzuRg4PpnQMedHpBiZGUi/bhyKXreo4m8=',2,'2026-07-27 07:56:01'),(67,'GFSB5u7e7fk5dEpC5wjZQEud1Ea1oHPKqvmJpSNopJI=',2,'2026-07-27 08:04:57'),(68,'v4sRjOgcYiqbj7Dfqz51Bvpgy/zq4ZDCywGP7dJFps0=',2,'2026-07-27 08:30:27'),(69,'CHDAUrVD1tDODjhWbGBB4jlChGPfLyBRmrdDdGnOrYA=',2,'2026-07-27 08:33:17'),(70,'DK/r8/QOIhhuwde3ul3rCvk2WhbTAfcdATxE3mzDa7U=',2,'2026-07-27 08:34:48'),(71,'mCbVMeuUGkFTB9/0DBCYT7Su+Uf6QhiHDtRU7Z5u1PU=',2,'2026-07-27 10:06:59'),(72,'UaJ9fa8MN9YiCUxLVuMxVRiQ4ILLe15CSjBMvnHOWv8=',2,'2026-07-27 10:11:56'),(73,'R6QwxKYt6BIfoxtt4Z4d98yOwiR6Thf3IFK2frPaq9c=',2,'2026-07-27 10:21:55'),(74,'fOWwG1kR6SHNvzliz140l+8JOt8DGmLmUKC8DuwNGlg=',2,'2026-07-27 10:22:50'),(75,'jzvCx4eKSGjKZFZW71V/u0psdEYkLoM8IiIfUpnXzCc=',2,'2026-07-27 10:23:45'),(76,'FBRXak2Mnmm9wqTqAL7tpsDOy2+gv7daqC5OXFJmOjc=',2,'2026-07-27 10:26:39'),(77,'+9zDs+29LRVjTu6jOxpXJOVi22MfzF1sPDd9y3KoVzY=',2,'2026-07-27 10:27:24'),(78,'9ulutTQJz0yCA7j9o6jYq5p0/78sss2pd59AmAV9pOo=',2,'2026-07-27 10:30:52'),(79,'6k1H9Je7CfE6iQbudLITUa8enPPi+f/rXfgdcxnnEKA=',2,'2026-07-27 10:33:32'),(80,'cDpuT3b1a158tA0T6vXxg9WQfcoKcbMb9PfRQ7IUuic=',2,'2026-07-27 10:37:33'),(81,'7Aw4GGvla3mTjI9bbvORhqFiOpVWke0uvr9RgjZXIwo=',2,'2026-07-27 10:45:35'),(82,'JSkCc7KGNTG1yqeBoZuxJuZBaju1qSWCZbP0TWVu8To=',2,'2026-07-27 11:57:38'),(83,'M0sAwjgRKshQUfrrZQK712QWRiZMF1LAxL3L83gNm2U=',2,'2026-07-27 11:58:31'),(84,'ruBcmnjWMPAxJ/icyd3uLkrbL1AqzzPZdLP94Sp45r4=',1,'2026-07-27 12:00:26');
/*!40000 ALTER TABLE `refresh_tokens` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `users` (
  `id` int NOT NULL AUTO_INCREMENT,
  `username` varchar(50) NOT NULL,
  `password` varchar(255) NOT NULL,
  `role` enum('DOCTOR','ACCOUNTANT','ADMIN') NOT NULL,
  `enabled` tinyint(1) DEFAULT '1',
  PRIMARY KEY (`id`),
  UNIQUE KEY `username` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=48 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` VALUES (1,'admin','$2a$12$b4Np/tnxalo04DJccAdwJOGjF3bC0HFR/bM2hTJW8NxO9xOukHNJO','ADMIN',1),(2,'yazan','$2a$10$Mydq0yvntfhePbRVLwZ7r.uJApLc83E2u3LhLcxKmWHTGQ//AmGt6','DOCTOR',1),(3,'mahmoud','$2a$10$MnPOloBhoTyhhGsfH660r.5592kovopnQcV9ST6RLZfEXjA9Ws56e','DOCTOR',1),(4,'moath_doc','$2a$10$yD2xFrETUGeodo8ekobMruXRgbncf.uHWQPABMoXdRmq4OmfWkKB6','DOCTOR',1),(6,'moathss_doc','$2a$10$kvfofek6ik12.CQSah9PKeOCcLWBIkH8Cby/.fOgYH287uJhxP3a2','DOCTOR',1),(7,'sami_zaid','$2a$10$tnzTVyREsuvyzHT/2/bUNu6HredEOtixS3WtLGgW.VI375bteDaG2','DOCTOR',1),(8,'sami_zaid_username','$2a$10$7Ini46ILkibzJQDLG01/KO30Il86hz274W1XpkqtERDv6JTwW8EqS','DOCTOR',1);
/*!40000 ALTER TABLE `users` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-07-20 15:50:54
