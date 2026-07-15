package com.example.clinicmvcspring.controllers;


import com.example.clinicmvcspring.models.AppUser;
import com.example.clinicmvcspring.services.AppUserDetailsService;
import com.example.clinicmvcspring.services.DoctorService;
import com.example.clinicmvcspring.services.JwtService;
import com.example.clinicmvcspring.services.RefreshTokenService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@WebMvcTest(UserController.class)
@AutoConfigureMockMvc(addFilters = false) // disables Spring Security
public class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;
//in mockMvc assert and act phases are together
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
    public void UserController_GetAllUsers_ReturnsOk()throws Exception
    {
        // arrange
        Page<AppUser> fakePage = new PageImpl<>(Collections.emptyList());
        when(userService.getAllUsers(any())).thenReturn(fakePage);


        // act and assert

        // ACT & ASSERT
        mockMvc.perform(get("/api/v1/users"))
                .andExpect(status().isOk());

    }


    @Test
    public void UserController_GetAllUsersWithNegativePage_ReturnsBadRequest() throws Exception
    {
        // arrange
        // nothing i want to only test bad response based on bad page number in url

        // act and assert

        mockMvc.perform(get("/api/v1/users?page=-1"))
                .andExpect(status().is(400))
                .andExpect(jsonPath("$.message").value("Page Number can't be negative"));



    }



}
