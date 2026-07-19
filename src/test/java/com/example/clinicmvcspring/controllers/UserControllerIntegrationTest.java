package com.example.clinicmvcspring.controllers;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.clinicmvcspring.dtos.LoginRequest;
import com.example.clinicmvcspring.models.AppUser;
import com.example.clinicmvcspring.models.Role;
import com.example.clinicmvcspring.repositories.RefreshTokenRepo;
import com.example.clinicmvcspring.repositories.UserRepo;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT) // random port for testing
@AutoConfigureMockMvc
public class UserControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private RefreshTokenRepo refreshTokenRepo;

    @BeforeEach
    void setUp() {
        refreshTokenRepo.deleteAll();// foreign key constraint

        userRepo.deleteAll();// in case of crash delete tests

        AppUser testUser = new AppUser();
        testUser.setUsername("integration-test-user");

        testUser.setPassword(passwordEncoder.encode("test-password"));
        testUser.setRole(Role.ADMIN);
        userRepo.save(testUser);

    }

    @AfterEach
    void cleanUp() {
        refreshTokenRepo.deleteAll();
        userRepo.deleteAll();// delete tests

    }

    // Test login

    @Test
    void UserControllerIntegration_Login_ReturnsOkWithToken() throws Exception {

        // ARRANGE
        LoginRequest request = new LoginRequest();
        request.setUsername("integration-test-user");
        request.setPassword("test-password");

        // act + assert

        mockMvc.perform(post("/api/v1/users/login")
                .contentType(MediaType.APPLICATION_JSON)// req body of post in perform
                .content("""
                        {
                            "username": "integration-test-user",
                            "password": "test-password"
                        }
                        """)// JSON BODY REQ
        )
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.token").exists())
                .andExpect(jsonPath("$.refreshToken").exists());
    }

    @Test
    void UserControllerIntegration_registerUser_ReturnsCreatedSuccessfully() throws Exception {
        // ARRANGE

        // act + assert
        mockMvc.perform(post("/api/v1/users/signup")
                .contentType(MediaType.APPLICATION_JSON)// req body of post in perform
                .content("""
                        {
                            "username": "test-username",
                            "password": "test-password",
                            "role": "ADMIN"
                        }
                                                    """)// JSON BODY REQ
        )
                .andExpect(status().is(201))
                .andExpect(jsonPath("$.status").exists())
                .andExpect(jsonPath("$.message").value("User registered successfully!"));
    }

}
