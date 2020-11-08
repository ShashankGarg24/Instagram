package com.instagram.models;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;
import java.util.UUID;

@Entity
public class UserCredentials {

    @Id
    @Column(nullable = false, unique = true)
    private UUID userId;
    @Column(nullable = false)
    private String userEmail;
    @Column(nullable = false)
    private String userPassword;
    private String role;
    private boolean verified;


    public UserCredentials() {
    }

    public UserCredentials(String userEmail, String userPassword, String role, boolean verified) {
        this.userId = UUID.randomUUID();
        this.userEmail = userEmail;
        this.userPassword = userPassword;
        this.role = role;
        this.verified = verified;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public boolean isVerified() {
        return verified;
    }

    public void setVerified(boolean verified) {
        this.verified = verified;
    }
}
