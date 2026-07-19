package com.example.clinicmvcspring.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.example.clinicmvcspring.services.AppUserDetailsService;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    private final CustomAuthEntryPoint customAuthEntryPoint;
    private final CustomAccessDeniedHandler customAccessDeniedHandler;
    private final AppUserDetailsService appUserDetailsService;
    private final PasswordEncoder passwordEncoder;
    private final JwtAuthFilter jwtAuthFilter;

    public SecurityConfig(CustomAuthEntryPoint customAuthEntryPoint, AppUserDetailsService appUserDetailsService,
            PasswordEncoder passwordEncoder, JwtAuthFilter jwtAuthFilter, CustomAccessDeniedHandler customAccessDeniedHandler) {
        this.customAuthEntryPoint = customAuthEntryPoint;
        this.appUserDetailsService = appUserDetailsService;
        this.passwordEncoder = passwordEncoder;
        this.jwtAuthFilter = jwtAuthFilter;
        this.customAccessDeniedHandler = customAccessDeniedHandler;
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        // built in from spring spring security, a class that does the auth (get
        // credentials and compare)
        // DaoAuthenticationProvider needs a class that implements UserDetailsService

        // (appUserDetailService)
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider(appUserDetailsService);
        // DaoAuthenticationProvider needs password encoder for password hashing
        provider.setPasswordEncoder(passwordEncoder);
        return provider;
    }

    // Manager that delegates the authentication obj to providers
    // this way i can mae use of multiple authProvidors in my system
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager(); // Spring automatically links the provider above
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authenticationProvider(authenticationProvider())
                .csrf(csrf -> csrf.disable())// disable cross site request forgery protection for postman
                .authorizeHttpRequests(req -> req // AUTHORIZATION
                        .requestMatchers("/api/v1/users/login").permitAll() // so all users can login
                        .requestMatchers("/api/v1/users/logout").permitAll() // so all users can logout
                        .requestMatchers("/api/v1/users/refresh").permitAll()// so all users can refresh their tokens
                        .requestMatchers("/api/v1/doctors/**").hasRole("ADMIN")// (**)means that any endpoint with this
                                                                               // path
                        .requestMatchers("/api/v1/patients/**").hasRole("ADMIN")
                        .requestMatchers("/api/v1/medications/**").hasRole("ADMIN")
                        .requestMatchers("/api/v1/appointments/**").hasAnyRole("ADMIN", "DOCTOR")
                        .requestMatchers("/api/v1/prescriptions/**").hasAnyRole("ADMIN", "DOCTOR")
                        .requestMatchers("/api/v1/users/**").hasRole("ADMIN")

                        .anyRequest().authenticated())// everything else

                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint(customAuthEntryPoint)
                        .accessDeniedHandler(customAccessDeniedHandler))
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();

    }
}