package com.instagram.models;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.instagram.DTO.UserShortDetailsDTO;
import org.hibernate.annotations.*;
import org.springframework.context.annotation.Lazy;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import javax.annotation.Resource;
import javax.persistence.*;
import javax.persistence.CascadeType;
import javax.persistence.Entity;

@Entity
public class Posts {

  @Id
  @Column(nullable = false, unique = true)
  private UUID postId;
  private Integer likes = 0;
  @CreationTimestamp
  @JsonIgnore
  private LocalDateTime postCreationTimeStamp;
  @UpdateTimestamp
  @JsonIgnore
  private LocalDateTime postLastUpdateTimeStamp;
  private String location;
  private String caption;
  private boolean commentActivity;
  private boolean pinned;
  private String profilePicPathOfUploader;
  private String fullNameOfUploader;
  private String usernameOfUploader;


  @OneToMany(orphanRemoval = true)
  @JsonIgnore
  private List<LikeModel> userLikes = new ArrayList<>();

  @ManyToOne(fetch = FetchType.LAZY)
  @JsonIgnore
  private UserProfile profile;

  @OneToOne(fetch = FetchType.EAGER)
  private Media postMedia;

  @OneToMany(fetch = FetchType.LAZY, orphanRemoval = true)
  private List<Comment> comments = new ArrayList<>();

  //tagged users

 /* @OneToMany
  private List<Comment> comments;
*/

  public Posts() {

  }


  public Posts( String location, String caption, boolean commentActivity, boolean pinned, String profilePicPathOfUploader, String fullNameOfUploader, String usernameOfUploader) {
    this.postId = UUID.randomUUID();
    this.postCreationTimeStamp = LocalDateTime.now();
    this.postLastUpdateTimeStamp = LocalDateTime.now();
    this.location = location;
    this.caption = caption;
    this.commentActivity = commentActivity;
    this.pinned = pinned;
    this.profilePicPathOfUploader = profilePicPathOfUploader;
    this.fullNameOfUploader = fullNameOfUploader;
    this.usernameOfUploader = usernameOfUploader;
  }

  public UUID getPostId() {
    return postId;
  }

  public void setPostId(UUID postId) {
    this.postId = postId;
  }

  public Integer getLikes() {
    return likes;
  }

  public void increaseLikes() {
    ++this.likes;
  }

  public void decreaseLikes() {
    --this.likes;
  }

  public LocalDateTime getpostCreationTimeStamp() {
    return postCreationTimeStamp;
  }

  public void setpostCreationTimeStamp(LocalDateTime postCreationTimeStamp) {
    this.postCreationTimeStamp = postCreationTimeStamp;
  }

  public LocalDateTime getpostLastUpdateTimeStamp() {
    return postLastUpdateTimeStamp;
  }

  public void setpostLastUpdateTimeStamp(LocalDateTime postLastUpdateTimeStamp) {
    this.postLastUpdateTimeStamp = postLastUpdateTimeStamp;
  }

  public String getLocation() {
    return location;
  }

  public void setLocation(String location) {
    this.location = location;
  }

  public String getCaption() {
    return caption;
  }

  public void setCaption(String caption) {
    this.caption = caption;
  }

  public boolean isCommentActivity() {
    return commentActivity;
  }

  public void setCommentActivity(boolean commentActivity) {
    this.commentActivity = commentActivity;
  }


  public void setPostLastUpdateTimeStamp(LocalDateTime postLastUpdateTimeStamp) {
    this.postLastUpdateTimeStamp = postLastUpdateTimeStamp;
  }

  public String getProfilePicPathOfUploader() {
    return profilePicPathOfUploader;
  }

  public void setProfilePicPathOfUploader(String profilePicPathOfUploader) {
    this.profilePicPathOfUploader = profilePicPathOfUploader;
  }

  public String getFullNameOfUploader() {
    return fullNameOfUploader;
  }

  public void setFullNameOfUploader(String fullNameOfUploader) {
    this.fullNameOfUploader = fullNameOfUploader;
  }

  public String getUsernameOfUploader() {
    return usernameOfUploader;
  }

  public void setUsernameOfUploader(String usernameOfUploader) {
    this.usernameOfUploader = usernameOfUploader;
  }

  public UserProfile getProfile() {
    return profile;
  }

  public void setProfile(UserProfile profile) {
    this.profile = profile;
  }

  public boolean isPinned() {
    return pinned;
  }

  public void setPinned(boolean pinned) {
    this.pinned = pinned;
  }

  public Media getPostMedia() {
    return postMedia;
  }

  public void setPostMedia(Media postMedia) {
    this.postMedia = postMedia;
  }

  public List<LikeModel> getUserLikes() {
    return userLikes;
  }

  public void addUserLikes(LikeModel userLikes) {
    this.userLikes.add(userLikes);
  }

  public void removeUserLikes(LikeModel userLikes) {
    this.userLikes.remove(userLikes);
  }

  public List<Comment> getComments() {
    return comments;
  }

  public void addComment(Comment comment) {
    this.comments.add(comment);
  }

  public void removeComment(Comment comment) {
    this.comments.remove(comment);
  }
}
