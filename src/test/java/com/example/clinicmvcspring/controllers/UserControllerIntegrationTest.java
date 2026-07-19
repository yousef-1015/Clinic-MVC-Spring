package com.example.clinicmvcspring.controllers;

import java.util.Map;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.clinicmvcspring.models.AppUser;
import com.example.clinicmvcspring.models.Role;
import com.example.clinicmvcspring.repositories.RefreshTokenRepo;
import com.example.clinicmvcspring.repositories.UserRepo;

import tools.jackson.databind.ObjectMapper;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT) // random port for testing
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class UserControllerIntegrationTest {

        @Autowired
        private MockMvc mockMvc;

        @Autowired
        private PasswordEncoder passwordEncoder;

        @Autowired
        private UserRepo userRepo;

        @Autowired
        private RefreshTokenRepo refreshTokenRepo;

        @Autowired
        private ObjectMapper objectMapper;

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

        @Test
        void UserControllerIntegration_Login_ReturnsOkWithToken() throws Exception {

                // ARRANGE

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

                // ARRANGE get a real admin token first
                MvcResult loginResult = mockMvc.perform(post("/api/v1/users/login")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                                {
                                                    "username": "integration-test-user",
                                                    "password": "test-password"
                                                }
                                                """))
                                .andExpect(status().isOk())
                                .andReturn();

                String body = loginResult.getResponse().getContentAsString(); // raw JSON string
                Map<String, Object> loginBody = objectMapper.readValue(body, Map.class);
                String realJwtToken = (String) loginBody.get("token");

                // ACT + ASSERT
                mockMvc.perform(post("/api/v1/users/signup")
                                .contentType(MediaType.APPLICATION_JSON)
                                .header("Authorization", "Bearer " + realJwtToken) //
                                .content("""
                                                {
                                                    "username": "new-test-user",
                                                    "password": "test-password",
                                                    "role": "ADMIN"
                                                }
                                                """))
                                .andExpect(status().is(201))
                                .andExpect(jsonPath("$.message").value("User registered successfully!"));
        }

        @Test
        void UserControllerIntegration_Logout_ReturnsOkWithToken() throws Exception {
                // arrange
                // Login and capture the real response
                MvcResult loginResult = mockMvc.perform(post("/api/v1/users/login")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                                {
                                                    "username": "integration-test-user",
                                                    "password": "test-password"
                                                }
                                                """))
                                .andExpect(status().isOk())
                                .andReturn(); // get the full response as object

                String body = loginResult.getResponse().getContentAsString(); // raw JSON string
                Map<String, Object> loginBody = objectMapper.readValue(body, Map.class);
                String realJwtToken = (String) loginBody.get("token");
                String realRefreshToken = (String) loginBody.get("refreshToken");

                // Act + Assert
                mockMvc.perform(
                                post("/api/v1/users/logout")
                                                .contentType(MediaType.APPLICATION_JSON)
                                                .header("Authorization", "Bearer " + realJwtToken)
                                                .content("{ \"refreshToken\": \"" + realRefreshToken + "\" }"))
                                .andExpect(status().is(200))
                                .andExpect(jsonPath("$.message").value("Logged out successfully!"));

        }

        @Test
        void UserControllerIntegration_Refresh_ReturnsOkWithToken() throws Exception {

                // arrange
                // Login and capture the real response
                MvcResult loginResult = mockMvc.perform(post("/api/v1/users/login")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                                {
                                                    "username": "integration-test-user",
                                                    "password": "test-password"
                                                }
                                                """))
                                .andExpect(status().isOk())
                                .andReturn(); // get the full response as object

                String body = loginResult.getResponse().getContentAsString(); // raw JSON string
                Map<String, Object> loginBody = objectMapper.readValue(body, Map.class);
                String realRefreshToken = (String) loginBody.get("refreshToken");

                // act + assert
                mockMvc.perform(post("/api/v1/users/refresh")
                                .contentType(MediaType.APPLICATION_JSON)// req body of post in perform
                                .content("{ \"refreshToken\": \"" + realRefreshToken + "\" }"))

                                .andExpect(status().is(200))

                                .andExpect(jsonPath("$.newRefreshToken").exists())
                                .andExpect(jsonPath("$.newAccessToken").exists());

        }

        @Test
        void UserControllerIntegration_registerUser_NoTokenReturns401() throws Exception {
                // ACT + ASSERT
                mockMvc.perform(post("/api/v1/users/signup")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                                {
                                                    "username": "new-test-user",
                                                    "password": "test-password",
                                                    "role": "ADMIN"
                                                }
                                                """))
                                .andExpect(status().is(401))
                                .andExpect(jsonPath("$.error").value("Unauthorized"));
        }

        @Test
        void UserControllerIntegration_registerUser_BadRoleReturns403() throws Exception {

                // arrange
                AppUser fakeUser = new AppUser();
                fakeUser.setUsername("fake-username");

                fakeUser.setPassword(passwordEncoder.encode("fake-password"));
                fakeUser.setRole(Role.DOCTOR);
                userRepo.save(fakeUser);

                MvcResult loginResult = mockMvc.perform(post("/api/v1/users/login")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                                {
                                                "username": "fake-username",
                                                "password": "fake-password"
                                                }
                                                """))
                                .andExpect(status().isOk())
                                .andReturn();

                String body = loginResult.getResponse().getContentAsString();
                Map<String, Object> loginBody = objectMapper.readValue(body, Map.class);
                String realJwtToken = (String) loginBody.get("token");

                // ACT + ASSERT
                mockMvc.perform(post("/api/v1/users/signup")
                                .contentType(MediaType.APPLICATION_JSON)
                                .header("Authorization", "Bearer " + realJwtToken) //
                                .content("""
                                                {
                                                "username": "new-test-user",
                                                "password": "test-password",
                                                "role": "ADMIN"
                                                }
                                                """))
                                .andExpect(status().is(403))
                                .andExpect(jsonPath("$.error").value("Forbidden"))
                                .andExpect(jsonPath("$.message")
                                                .value("You do not have permission to access this resource"));

        }
}
