package com.example.clinicmvcspring.config;

import java.io.IOException;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.example.clinicmvcspring.services.JwtService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthFilter extends OncePerRequestFilter // so filter runs once on every request received
{
    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    public JwtAuthFilter(JwtService jwtService, UserDetailsService userDetailsService) {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        // Get the Authorization header from the request
        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String username;

        // If the header is missing or doesn't start with "Bearer ", pass it to the
        // next filter
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response); // Go to next filter
            return; // Exit this filter
        }

        // Extract the raw token (after Bearer)
        jwt = authHeader.substring(7); // "Bearer " is 7 characters long (6 letters and 1 white space)
        // Extract the username from the token
        username = jwtService.extractUsername(jwt);

        // if we have a username and the user is not already a signed in user
        // security context holder has the currently signed in user
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            // Load the user details from the database
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);

            // Verify if the token is valid (username and not expired)
            if (jwtService.validateToken(jwt, userDetails)) {

                // Create the official sign in Access Pass (Authentication Token)
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null, // We don't need credentials (password)
                        userDetails.getAuthorities() // get roles for authorizations\
                );

                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        // Go to next filter
        filterChain.doFilter(request, response);

    }
}
