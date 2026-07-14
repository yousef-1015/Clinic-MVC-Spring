package com.example.clinicmvcspring.models;

import java.sql.Timestamp;

import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
@Table(name = "refresh_tokens")
public class RefreshToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false, unique = true)
    private String token;
    @ManyToOne // one user can have many refresh tokens (login from multiple devices)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private AppUser user;
    @Column(nullable = false, name = "expiry_date")
    private Timestamp expiryDate;

    @Transient
    private String plainTextToken;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public AppUser getUser() {
        return user;
    }

    public void setUser(AppUser user) {
        this.user = user;
    }

    public Timestamp getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(Timestamp expiryDate) {
        this.expiryDate = expiryDate;
    }

    public String getPlainTextToken() {
        return plainTextToken;
    }

    public void setPlainTextToken(String plainTextToken) {
        this.plainTextToken = plainTextToken;
    }
}
