package com.example.clinicmvcspring.services;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.util.Base64;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.clinicmvcspring.models.AppUser;
import com.example.clinicmvcspring.models.RefreshToken;
import com.example.clinicmvcspring.repositories.RefreshTokenRepo;
import com.example.clinicmvcspring.repositories.UserRepo;

@Service
public class RefreshTokenService {

    @Value("${application.security.jwt.refresh-token.expiration}")
    private long refreshExpiration;

    private final RefreshTokenRepo refreshTokenRepo;
    private final UserRepo userRepo;

    public RefreshTokenService(RefreshTokenRepo refreshTokenRepo, UserRepo userRepo) {
        this.refreshTokenRepo = refreshTokenRepo;
        this.userRepo = userRepo;
    }

    public Optional<RefreshToken> findByToken(String token) {
        return refreshTokenRepo.findByToken(hashToken(token)); // sreach by hashed one in db
    }

    @Transactional
    public RefreshToken createRefreshToken(String username) {
        AppUser user = userRepo.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found: " + username));

        // refreshTokenRepo.deleteByUser(user);// delete old token
        // NOW USERS CAN HAVE MULTIPLE REFRESH TOKENS (MULTI DEVICE LOGIN)

        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setUser(user);
        String rawToken = UUID.randomUUID().toString();
        refreshToken.setPlainTextToken(rawToken);
        refreshToken.setToken(hashToken(rawToken));
        refreshToken.setExpiryDate(new Timestamp(System.currentTimeMillis() + refreshExpiration));

        return refreshTokenRepo.save(refreshToken);
    }

    public RefreshToken verifyExpiration(RefreshToken token) {
        if (token.getExpiryDate().before(new Timestamp(System.currentTimeMillis()))) {
            refreshTokenRepo.delete(token);
            throw new RuntimeException("Refresh token was expired. Please sign in again.");
        }
        return token;
    }

    @Transactional
    public int deleteByUsername(String username) {
        return userRepo.findByUsername(username)
                .map(refreshTokenRepo::deleteByUser)
                .orElse(0);
    }

    // This cron expression means: run at 3:00 AM every day
    // second minute hour dayOfTheMonth Month DayOfTheWeek
    @Scheduled(cron = "0 0 3 * * *")
    public void cleanUpExpiredTokens() {
        int deletedTokens = refreshTokenRepo.deleteAllExpiredTokens();
        System.out.println("Cleanup: Deleted " + deletedTokens + " expired tokens.");
    }

    public void deleteToken(RefreshToken token) {
        refreshTokenRepo.delete(token);
    }

    private String hashToken(String token) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256"); // hashing algo not same as BCRYPT
            byte[] hash = digest.digest(token.getBytes());// digest only takes bytes not string
            return Base64.getEncoder().encodeToString(hash);// from bytes to string
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Hashing error", e);
        }
    }
}