package com.instagram.models;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import javax.persistence.*;

@Entity
public class User{


  @Id
  @Column(nullable = false, unique = true)
  private UUID userId;
  private String fullName;
  @Column(unique = true)
  private String username;
  @Column(nullable = false)
  private String userEmail;
  @Column(nullable = false)
  private String userPassword;
  @CreationTimestamp
  private LocalDateTime userCreationTimeStamp;
  private String role;
  private String userBio;
  private String userPrivacy;
  private String verificationToken;
  private boolean enabled;
  private boolean verified;

  @OneToOne(fetch = FetchType.LAZY,  cascade = CascadeType.ALL)
  private Media profilePic;

  @OneToMany(mappedBy = "postByUser")
  private List<Posts> posts;

  @OneToMany(mappedBy = "commentByUser")
  private List<Comment> comments;

  @OneToMany(mappedBy = "subCommentByUser")
  private List<SubComment> subComments;



  //followers
  //followings



  public User() {
    this.userCreationTimeStamp = LocalDateTime.now();
  }



  public UUID getUserId() {
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

  public LocalDateTime getUserCreationTimeStamp() {
    return userCreationTimeStamp;
  }

  public void setUserCreationTimeStamp(LocalDateTime userCreationTimeStamp) {
    this.userCreationTimeStamp = userCreationTimeStamp;
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

  public Media getProfilePic() {
    return profilePic;
  }

  public void setProfilePic(Media profilePic) {
    this.profilePic = profilePic;
  }

  public List<Posts> getPosts() {
    return posts;
  }

  public void addPost(Posts post) {
    this.posts.add(post);
  }

  public void removePost(Posts post) {
    this.posts.remove(post);
  }

}
