package com.example.clinicmvcspring.services;

import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import com.example.clinicmvcspring.models.AppUser;
import com.example.clinicmvcspring.models.CustomUserDetails;
import com.example.clinicmvcspring.models.Role;

@ExtendWith(MockitoExtension.class)
public class JwtServiceTest {
    private JwtService jwtService;
    private CustomUserDetails fakeUserDetails;
    private AppUser fakeAppUser;

    @BeforeEach // for each test create these new object
    void setUp() {
        jwtService = new JwtService();
        fakeAppUser = new AppUser();
        fakeAppUser.setUsername("fake-name");
        fakeAppUser.setId(55);
        fakeAppUser.setRole(Role.DOCTOR);
        fakeUserDetails = new CustomUserDetails(fakeAppUser);

        ReflectionTestUtils.setField(jwtService, "jwtSecret",
                "339b66b1e4668bca8357465af57f141f538f6def6abb1b7c7d5fce7173bbfb67");
    }

    @Test
    void JwtService_generateToken_TokenNotEmpty() {
        // Arrange

        // act
        String token = jwtService.generateToken(fakeUserDetails);

        // assert
        assertThat(token).isNotEmpty();
    }

    // arrange
    // act
    // assert
    @Test
    void JwtService_generateToken_TokenContainsUsername() {
        // Arrange
        // in setUp()

        String token = jwtService.generateToken(fakeUserDetails);

        // act
        String extractedUsername = jwtService.extractUsername(token);
        // assert
        assertThat(extractedUsername).isEqualTo(fakeUserDetails.getUsername());
    }

    @Test
    void JwtService_generateToken_TokenContainsRole() {

        // Arrange
        // in setUp()

        String token = jwtService.generateToken(fakeUserDetails);

        // Act
        String extractedRole = jwtService.extractRole(token);

        // Assert
        assertThat(extractedRole).isEqualTo("ROLE_" + fakeUserDetails.getAppUser().getRole().name());

    }

    @Test
    void JwtService_generateToken_TokenContainsExpiration() {

        // Arrange
        // in setUp()
        String token = jwtService.generateToken(fakeUserDetails);
        // Act
        Date extractedExpiration = jwtService.extractExpirationDate(token);

        // Assert expiration must be in the future (after now)
        assertThat(extractedExpiration).isAfter(new Date()); 
    }
}
