package com.example.clinicmvcspring.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.example.clinicmvcspring.services.AppUserDetailsService;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    private final CustomAuthEntryPoint customAuthEntryPoint;
    private final AppUserDetailsService appUserDetailsService;
    private final PasswordEncoder passwordEncoder;

    public SecurityConfig(CustomAuthEntryPoint customAuthEntryPoint, AppUserDetailsService appUserDetailsService,
            PasswordEncoder passwordEncoder) {
        this.customAuthEntryPoint = customAuthEntryPoint;
        this.appUserDetailsService = appUserDetailsService;
        this.passwordEncoder = passwordEncoder;
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        // built in from spring spring security, a class that does the auth (get
        // credentials and compare)
        // DaoAuthenticationProvider needs a class that implements UserDetails service
        // (appUserDetailService)
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider(appUserDetailsService);
        // DaoAuthenticationProvider needs password encoder for password hashing
        provider.setPasswordEncoder(passwordEncoder);
        return provider;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authenticationProvider(authenticationProvider())
                .csrf(csrf -> csrf.disable())// disable cross site request forgery protection for postman
                .authorizeHttpRequests(req -> req // AUTHORIZATION
                        .requestMatchers("/api/v1/doctors/**").hasRole("ADMIN")// (**)means that any endpoint with this
                                                                               // path
                        .requestMatchers("/api/v1/patients/**").hasRole("ADMIN")
                        .requestMatchers("/api/v1/medications/**").hasRole("ADMIN")
                        .requestMatchers("/api/v1/appointments/**").hasAnyRole("ADMIN", "DOCTOR")
                        .requestMatchers("/api/v1/prescriptions/**").hasAnyRole("ADMIN", "DOCTOR")
                        .requestMatchers("/api/v1/users/signup").hasRole("ADMIN")

                        .anyRequest().authenticated())// everything else

                .httpBasic(basic -> {
                })// use basic username and password in the request header
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint(customAuthEntryPoint));

        return http.build();

    }
}