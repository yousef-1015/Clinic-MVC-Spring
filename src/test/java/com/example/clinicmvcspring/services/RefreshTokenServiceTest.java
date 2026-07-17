package com.example.clinicmvcspring.services;

import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.clinicmvcspring.models.AppUser;
import com.example.clinicmvcspring.models.RefreshToken;
import com.example.clinicmvcspring.models.Role;
import com.example.clinicmvcspring.repositories.RefreshTokenRepo;
import com.example.clinicmvcspring.repositories.UserRepo;

@ExtendWith(MockitoExtension.class)

public class RefreshTokenServiceTest {
    @Mock // to mock repo and only tes the service
    private RefreshTokenRepo refreshTokenRepo;
    @Mock
    private UserRepo userRepo;

    @InjectMocks // use the mock i made (annotated)
    private RefreshTokenService refreshTokenService;

    @Test
    public void RefreshTokenService_CreateRefreshToken_RefreshTokenSaved() {
        // arrange
        AppUser user = new AppUser();
        user.setUsername("testUsername");
        user.setPassword("testPassword");
        user.setRole(Role.DOCTOR);

        // for the AppUser user = userRepo.findByUsername(username) to not throw an
        // exception
        when(userRepo.findByUsername("testUsername"))
                .thenReturn(Optional.of(user));

        when(refreshTokenRepo.save(any(RefreshToken.class))) // any token
                .thenReturn(new RefreshToken());

        // act
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(user.getUsername());

        // assert
        Assertions.assertNotNull(refreshToken);

    }

    @Test
    public void RefreshTokenService_DeleteRefreshToken_RefreshTokenDeleted() {
        // arrange
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setToken("fake-test-token");

        // act
        refreshTokenService.deleteToken(refreshToken);

        // assert
        // make sure the repo only did one delete operation
        verify(refreshTokenRepo, times(1)).delete(refreshToken);

    }
    // test user not found exception

    @Test
    public void RefreshTokenService_CreateRefreshToken_ThrowsExceptionWhenUserNotFound() {
        // arrange
        when(userRepo.findByUsername("non-existing-username"))
                .thenReturn(Optional.empty());
        // act + assert
        Assertions.assertThrows(RuntimeException.class, () -> {
            // code that will cause exception
            refreshTokenService.createRefreshToken("non-existing-username");
        });

    }
}