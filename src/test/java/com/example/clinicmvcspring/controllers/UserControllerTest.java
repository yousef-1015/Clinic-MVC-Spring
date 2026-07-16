package com.example.clinicmvcspring.controllers;

import java.util.Collections;

import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.clinicmvcspring.models.AppUser;
import com.example.clinicmvcspring.models.Role;

import com.example.clinicmvcspring.models.CustomUserDetails;
import com.example.clinicmvcspring.models.RefreshToken;
import com.example.clinicmvcspring.services.AppUserDetailsService;
import com.example.clinicmvcspring.services.DoctorService;
import com.example.clinicmvcspring.services.JwtService;
import com.example.clinicmvcspring.services.RefreshTokenService;

@WebMvcTest(UserController.class)
@AutoConfigureMockMvc(addFilters = false) // disables Spring Security
public class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;
    // in mockMvc assert and act phases are together
    @MockitoBean
    private AppUserDetailsService userService;
    @MockitoBean
    private DoctorService doctorService;
    @MockitoBean
    private AuthenticationManager authenticationManager;
    @MockitoBean
    private JwtService jwtService;
    @MockitoBean
    private RefreshTokenService refreshTokenService;

    // test get all users endpoint

    @Test
    public void UserController_GetAllUsers_ReturnsOk() throws Exception {
        // arrange
        Page<AppUser> fakePage = new PageImpl<>(Collections.emptyList());
        when(userService.getAllUsers(any())).thenReturn(fakePage);

        // act and assert

        mockMvc.perform(get("/api/v1/users"))
                .andExpect(status().isOk());

    }

    @Test
    public void UserController_GetAllUsersWithNegativePage_ReturnsBadRequest() throws Exception {
        // arrange
        // nothing i want to only test bad response based on bad page number in url

        // act and assert

        mockMvc.perform(get("/api/v1/users?page=-1"))
                .andExpect(status().is(400))
                .andExpect(jsonPath("$.message").value("Page Number can't be negative"));

    }

    @Test
    public void UserController_registerUser_ReturnsCreatedSuccessfully() throws Exception {
        // arrange
        AppUser fakeSavedUser = new AppUser();
        fakeSavedUser.setId(55);
        when(userService.addUser(any())).thenReturn(fakeSavedUser);

        // act and assert
        mockMvc.perform(post("/api/v1/users/signup")
                .contentType(MediaType.APPLICATION_JSON)// req body of post in perform
                .content("""
                        {
                            "username": "name",
                            "password": "1234",
                            "role": "DOCTOR"
                        }
                        """)// JSON BODY REQ
        )
                .andExpect(status().is(201))
                .andExpect(jsonPath("$.message").value("User registered successfully!"));

    }

    // Test generate Access token

    @Test
    public void UserController_login_GenerateTokenSuccessfully() throws Exception {
        AppUser fakeAppUser = new AppUser();
        fakeAppUser.setUsername("fake-name");
        fakeAppUser.setId(55);
        fakeAppUser.setRole(Role.DOCTOR);
        CustomUserDetails fakeUserDetails = new CustomUserDetails(fakeAppUser);
        RefreshToken fakeRefreshToken = new RefreshToken();
        fakeRefreshToken.setPlainTextToken("fake-refresh-token");

        when(userService.loadUserByUsername("fake-name")).thenReturn(fakeUserDetails);
        when(refreshTokenService.createRefreshToken("fake-name")).thenReturn(fakeRefreshToken);
        when(jwtService.generateToken(fakeUserDetails)).thenReturn("fake-jwt-token");

        // act + assert

        mockMvc.perform(post("/api/v1/users/login")
                .contentType(MediaType.APPLICATION_JSON)// req body of post in perform
                .content("""
                        {
                            "username": "fake-name",
                            "password": "1234"
                        }
                        """)// JSON BODY REQ
        )
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.token").value("fake-jwt-token"))
                .andExpect(jsonPath("$.refreshToken").value("fake-refresh-token"));

    }

}
