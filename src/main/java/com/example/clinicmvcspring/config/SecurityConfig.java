package com.example.clinicmvcspring.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    private final CustomAuthEntryPoint customAuthEntryPoint;

    public SecurityConfig(CustomAuthEntryPoint customAuthEntryPoint) {
        this.customAuthEntryPoint = customAuthEntryPoint;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();// hashing algo
    }

    @Bean
    public UserDetailsService userDetailsService(PasswordEncoder encoder) {

        var admin = User.builder()
                .username("admin")
                .password(encoder.encode("admin123")).roles("ADMIN")
                .build();

        var doctor = User.builder()
                .username("doctor")
                .password(encoder.encode("doc123"))
                .roles("DOCTOR")
                .build();

        return new InMemoryUserDetailsManager(admin, doctor);// for now (future: using my db)
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable())// disable cross site request forgery protection for postman
                .authorizeHttpRequests(req -> req // AUTHORIZATION
                        .requestMatchers("/api/v1/doctors/**").hasRole("ADMIN")//(**)means that any endpoint with this path 
                        .requestMatchers("/api/v1/patients/**").hasRole("ADMIN")
                        .requestMatchers("/api/v1/medications/**").hasRole("ADMIN")
                        .requestMatchers("/api/v1/appointments/**").hasAnyRole("ADMIN", "DOCTOR")
                        .requestMatchers("/api/v1/prescriptions/**").hasAnyRole("ADMIN", "DOCTOR")

                        .anyRequest().authenticated())// everything else 

                .httpBasic(basic -> {
                })// use basic username and password in the request header
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint(customAuthEntryPoint));

        return http.build();

    }
}