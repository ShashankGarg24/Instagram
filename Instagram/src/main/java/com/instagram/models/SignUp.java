package com.instagram.models;

public class SignUp {

  private String userEmail;
  private String password;
  private String confirmPassword;
  private String role;

  public SignUp(String userEmail, String password,
      String confirmPassword, String role) {
    this.userEmail = userEmail;
    this.password = password;
    this.confirmPassword = confirmPassword;
    this.role = role;
  }


  public String getUserEmail() {
    return userEmail;
  }

  public void setUserEmail(String userEmail) {
    this.userEmail = userEmail;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getConfirmPassword() {
    return confirmPassword;
  }

  public void setConfirmPassword(String confirmPassword) {
    this.confirmPassword = confirmPassword;
  }

  public String getRole() {
    return role;
  }

  public void setRole(String role) {
    this.role = role;
  }
}
