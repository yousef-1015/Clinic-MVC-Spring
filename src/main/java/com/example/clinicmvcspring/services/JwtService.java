package com.example.clinicmvcspring.services;

import java.security.Key;
import java.util.Date;
import java.util.HashSet;
import java.util.HexFormat;
import java.util.Set;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.example.clinicmvcspring.annotations.Audit;
import com.example.clinicmvcspring.events.UserLoggedInEvent;
import com.example.clinicmvcspring.models.AuditAction;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class JwtService {
    // Read the key from application.properties
    @Value("${application.security.jwt.secret-key}") // this value is a random hex value
    private String jwtSecret;
    // A set to store logged Out tokens in memory
    private final Set<String> tokenBlacklist = new HashSet<>();
    private final ApplicationEventPublisher applicationEventPublisher;

    private Key getSigningKey() {
        byte[] keyBytes = HexFormat.of().parseHex(jwtSecret);// decode the hex values into a an array of bytes
        return Keys.hmacShaKeyFor(keyBytes); // convert these bytes into a key object to use in java security
    }

    // @Audit(action = AuditAction.LOGIN)
    // Generates a token using the user details
    public String generateToken(UserDetails userDetails) {
        // Get the user role
        String role = userDetails.getAuthorities().iterator().next().getAuthority();
        // Build the token
        String token = Jwts.builder()
                .claim("role", role) // Add the role
                .setSubject(userDetails.getUsername()) // Add the username
                .setIssuedAt(new Date(System.currentTimeMillis())) // Created now
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24)) // Expire in 24 hours
                .signWith(getSigningKey(), SignatureAlgorithm.HS256) // Sign with key
                .compact(); // Build into a String
        publishUserLogInEvent(userDetails);
        return token;

    }

    public String extractUsername(String token) {
        // i use these 5 lines are always when getting a value from the token
        return Jwts.parserBuilder()// create the token reader
                .setSigningKey(getSigningKey())// use the key to see if the yoken was tampered with
                .build()// compile parses (reader)
                .parseClaimsJws(token)// open the token
                .getBody()// claims in payload of the token
                .getSubject();// get any field i want (username here)
    }

    public boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token)
                && !tokenBlacklist.contains(token));// check after logout
    }

    private boolean isTokenExpired(String token) {
        Date expiration = Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getExpiration();
        return expiration.before(new Date());
    }

    // Adds a token to the blacklist AFTER logout
    @Audit(action = AuditAction.LOGOUT)
    public void invalidateToken(String token) {
        tokenBlacklist.add(token);
        System.out.println("Token blacklisted: " + token);
    }

    public String extractRole(String token) {
        // i use these 5 lines are always when getting a value from the token
        return Jwts.parserBuilder()// create the token reader
                .setSigningKey(getSigningKey())// use the key to see if the yoken was tampered with
                .build()// compile parses (reader)
                .parseClaimsJws(token)// open the token
                .getBody()// claims in payload of the token
                .get("role", String.class); // extract the "role" claim
        // result will be "ROLE_"
    }

    public Date extractExpirationDate(String token) {

        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getExpiration();
    }

    // publish events
    private void publishUserLogInEvent(UserDetails userDetails) {
        UserLoggedInEvent userLoggedInEvent = new UserLoggedInEvent(userDetails.getUsername().strip(),
                "Logged In Successfully");
        applicationEventPublisher.publishEvent(userLoggedInEvent);
    }
}
