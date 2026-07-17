package com.example.clinicmvcspring.services;

import com.example.clinicmvcspring.models.AppUser;
import com.example.clinicmvcspring.models.Role;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.example.clinicmvcspring.repositories.UserRepo;

@ExtendWith(MockitoExtension.class)
public class AppUserDetailsServiceTest {

    @Mock
    private UserRepo userRepo;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AppUserDetailsService appUserDetailsService;

    @Test
    void AppUserDetailsService_loadUserByUsername_userExists() {
        // arrange

        AppUser fakeUser = new AppUser();
        fakeUser.setUsername("testUsername");
        fakeUser.setPassword("testPassword");
        fakeUser.setRole(Role.DOCTOR);

        when(userRepo.findByUsername("testUsername")).thenReturn(Optional.of(fakeUser));

        // act
        UserDetails fakeUserDetails = appUserDetailsService.loadUserByUsername("testUsername");

        // assert
        assertThat(fakeUserDetails).isNotNull();
        assertThat(fakeUserDetails.getUsername()).isEqualTo("testUsername");
    }

    @Test
    void AppUserDetailsService_loadUserByUsername_userDoesNotExists() {
        // arrange
        when(userRepo.findByUsername("non-existent-username")).thenReturn(Optional.empty());

        // act

        // assert
        assertThrows(UsernameNotFoundException.class, () -> {
            appUserDetailsService.loadUserByUsername("non-existent-username");

        });
    }

}
