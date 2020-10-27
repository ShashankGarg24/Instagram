package com.instagram.models;

import java.util.UUID;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class User {

  @Id
  private UUID userId;
  private String fullName;
  private String username;
  private String userEmail;
  private String userPassword;
  private String role;
  private String userBio;
  private String userPrivacy;
  private String verificationToken;
  private boolean enabled;
  private boolean verified;
  //posts
  //followers
  //followings


  public UUID getUserId(UUID uuid) {
    return userId;
  }

  public void setUserId(UUID userId) {
    this.userId = userId;
  }

  public String getFullName() {
    return fullName;
  }

  public void setFullName(String fullName) {
    this.fullName = fullName;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
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

  public String getUserBio() {
    return userBio;
  }

  public void setUserBio(String userBio) {
    this.userBio = userBio;
  }

  public String getUserPrivacy() {
    return userPrivacy;
  }

  public void setUserPrivacy(String userPrivacy) {
    this.userPrivacy = userPrivacy;
  }

  public String getVerificationToken() {
    return verificationToken;
  }

  public void setVerificationToken(String verificationToken) {
    this.verificationToken = verificationToken;
  }

  public boolean isEnabled() {
    return enabled;
  }

  public void setEnabled(boolean enabled) {
    this.enabled = enabled;
  }

  public boolean isVerified() {
    return verified;
  }

  public void setVerified(boolean verified) {
    this.verified = verified;
  }
}
