package com.example.clinicmvcspring.controllers;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.cache.CacheManager;
import org.springframework.cache.support.NoOpCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.clinicmvcspring.dtos.DoctorDTO;
import com.example.clinicmvcspring.models.Doctor;
import com.example.clinicmvcspring.services.AppUserDetailsService;
import com.example.clinicmvcspring.services.DoctorService;
import com.example.clinicmvcspring.services.JwtService;

@WebMvcTest(DoctorController.class)
@AutoConfigureMockMvc(addFilters = false) // disables Spring Security
@ActiveProfiles("test")

public class DoctorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private DoctorService doctorService;
    @MockitoBean
    private AppUserDetailsService appUserDetailsService;

    @MockitoBean
    private JwtService jwtService;

    @MockitoBean
    private com.example.clinicmvcspring.services.RefreshTokenService refreshTokenService;

    @TestConfiguration
    static class CacheTestConfig {
        @Bean
        public CacheManager cacheManager() {
            return new NoOpCacheManager();
        }
    }

    @Test
    public void DoctorController_GetAllDoctors_ReturnsOk() throws Exception {
        // arrange
        DoctorDTO fakeDoctor = new DoctorDTO(1, "John", "Doe", "john@clinic.com", "Cardiology");
        Page<DoctorDTO> fakePage = new PageImpl<>(List.of(fakeDoctor));
        when(doctorService.getAllDoctors(any())).thenReturn(fakePage);

        // act + assert
        mockMvc.perform(get("/api/v1/doctors"))
                .andExpect(status().isOk());
    }

    @Test
    public void DoctorController_GetAllDoctors_NegativePage_ReturnsBadRequest() throws Exception {
        // arrange - no mock needed, validation happens before service call

        // act + assert
        mockMvc.perform(get("/api/v1/doctors?page=-1"))
                .andExpect(status().is(400))
                .andExpect(jsonPath("$.message").value("Page Number Must Be Positive"));
    }

    @Test
    public void DoctorController_GetDoctorById_ReturnsOk() throws Exception {
        // arrange
        Doctor fakeDoctor = new Doctor(1, "John", "Doe", "john@clinic.com",
                new BigDecimal("15000.00"), null, "Cardiology");
        when(doctorService.getDoctorByID(1)).thenReturn(Optional.of(fakeDoctor));

        // act + assert
        mockMvc.perform(get("/api/v1/doctors/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.specialty").value("Cardiology"));
    }

    @Test
    public void DoctorController_AddDoctor_ReturnsCreated() throws Exception {
        // arrange
        Doctor savedDoctor = new Doctor(10, "Sara", "Smith", "sara@clinic.com",
                new BigDecimal("12000.00"), null, "Neurology");
        when(doctorService.addDoctor(any())).thenReturn(savedDoctor);

        // act + assert
        mockMvc.perform(post("/api/v1/doctors")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                            "firstName": "Sara",
                            "lastName": "Smith",
                            "email": "sara@clinic.com",
                            "salary": 1200.00,
                            "specialty": "Neurology"
                        }
                        """))
                .andExpect(status().is(201))
                .andExpect(jsonPath("$.firstName").value("Sara"));
    }

    @Test
    public void DoctorController_DeleteDoctor_ReturnsNoContent() throws Exception {
        // arrange
        Doctor fakeDoctor = new Doctor(1, "John", "Doe", "john@clinic.com",
                new BigDecimal("15000.00"), null, "Cardiology");
        when(doctorService.getDoctorByID(1)).thenReturn(Optional.of(fakeDoctor));

        // act + assert
        mockMvc.perform(delete("/api/v1/doctors/1"))
                .andExpect(status().is(204));

        verify(doctorService, times(1)).deleteDoctorByID(1);
    }

    @Test
    public void DoctorController_UpdateDoctor_ReturnsOk() throws Exception {
        // arrange - stub the existing doctor lookup (controller needs it for hireDate)
        Doctor existingDoctor = new Doctor(1, "John", "Doe", "john@clinic.com",
                new BigDecimal("15000.00"), null, "Cardiology");
        when(doctorService.getDoctorByID(1)).thenReturn(Optional.of(existingDoctor));

        // act + assert
        mockMvc.perform(put("/api/v1/doctors/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                            "firstName": "John",
                            "lastName": "Doe",
                            "email": "john.updated@clinic.com",
                            "salary": 1800.00,
                            "specialty": "Cardiology"
                        }
                        """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("john.updated@clinic.com"));

        verify(doctorService, times(1)).updateDoctorById(anyInt(), any());
    }

    @Test
    public void DoctorController_PatchDoctor_ReturnsOk() throws Exception {
        // arrange
        Doctor existingDoctor = new Doctor(1, "John", "Doe", "john@clinic.com",
                new BigDecimal("15000.00"), null, "Cardiology");
        when(doctorService.getDoctorByID(1)).thenReturn(Optional.of(existingDoctor));

        // act + assert
        mockMvc.perform(patch("/api/v1/doctors/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                            "specialty": "Neurology"
                        }
                        """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.specialty").value("Neurology"));

        verify(doctorService, times(1)).updateDoctorById(anyInt(), any());
    }

}
