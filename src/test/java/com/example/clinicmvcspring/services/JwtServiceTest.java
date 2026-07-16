package com.example.clinicmvcspring.services;

import java.security.Key;
import java.util.Date;
import java.util.HexFormat;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import com.example.clinicmvcspring.models.AppUser;
import com.example.clinicmvcspring.models.CustomUserDetails;
import com.example.clinicmvcspring.models.Role;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;

@ExtendWith(MockitoExtension.class)

public class JwtServiceTest {
    private JwtService jwtService;
    private CustomUserDetails fakeUserDetails;
    private AppUser fakeAppUser;
    private Key signingKey;

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

        byte[] keyBytes = HexFormat.of().parseHex("339b66b1e4668bca8357465af57f141f538f6def6abb1b7c7d5fce7173bbfb67");
        signingKey = Keys.hmacShaKeyFor(keyBytes);

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

    @Test
    void JwtService_validateToken_TokenIsRight() {

        // Arrange
        // in setUp()
        String token = jwtService.generateToken(fakeUserDetails);
        // Act
        Boolean isValid = jwtService.validateToken(token, fakeUserDetails);
        // assert

        assertThat(isValid).isTrue();

    }

    @Test
    void JwtService_validateToken_ThrowsExpiredJwtException() {
        // Arrange
        // in setUp()
        String expiredToken = Jwts.builder() // custom expired token to test expiration
                .claim("role", fakeUserDetails.getAppUser().getRole().name()) // Add the role
                .setSubject(fakeUserDetails.getUsername()) // Add the username
                .setIssuedAt(new Date(System.currentTimeMillis())) // Created now
                .setExpiration(new Date(System.currentTimeMillis() - 10000)) // Expired 10 seconds ago
                .signWith(signingKey, SignatureAlgorithm.HS256) // Sign with key
                .compact(); // Build into a String
        // Act
        // assert

        assertThrows(ExpiredJwtException.class, () -> {
            // .parseClaimsJws(token)// open the token throw ExpiredJwtException
            // if the token passed is expired
            jwtService.validateToken(expiredToken, fakeUserDetails);
        });

    }

    @Test
    void JwtService_validateToken_InvalidSignature() {

        // ARRANGE
        byte[] differentKeyBytes = HexFormat.of()
                .parseHex("aabbccddeeff00112233445566778899aabbccddeeff00112233445566778899");
        Key differentKey = Keys.hmacShaKeyFor(differentKeyBytes);

        String badToken = Jwts.builder()
                .setSubject(fakeUserDetails.getUsername())
                .setExpiration(new Date(System.currentTimeMillis() + 86400000))
                .signWith(differentKey, SignatureAlgorithm.HS256) // invalid key
                .compact();

        // Act
        // assert

        assertThrows(SignatureException.class, () -> {
            // .parseClaimsJws(token) throws SignatureException when the token's signature
            // doesn't
            // match the key
            jwtService.validateToken(badToken, fakeUserDetails);
        });
    }
}
