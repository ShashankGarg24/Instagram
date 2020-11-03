package com.instagram.models;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.ManyToAny;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.context.annotation.Lazy;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import javax.persistence.*;

@Entity
public class Posts {

  @Id
  @Column(nullable = false, unique = true)
  private UUID postId;
  private Integer likes = 0;
  @CreationTimestamp
  private LocalDateTime postCreationTimeStamp;
  @UpdateTimestamp
  private LocalDateTime postLastUpdateTimeStamp;
  private String location;
  private String caption;
  private boolean commentActivity;

  @OneToMany(mappedBy = "_post")
  private List<Media> Media;

  @ManyToOne
  private User postByUser;

  //tagged users

  @OneToMany(mappedBy = "post")
  private List<Comment> comments;


  protected Posts() {

  }


  public Posts( String location, String caption, boolean commentActivity) {
    this.postId = UUID.randomUUID();
    this.postCreationTimeStamp = LocalDateTime.now();
    this.postLastUpdateTimeStamp = LocalDateTime.now();
    this.location = location;
    this.caption = caption;
    this.commentActivity = commentActivity;
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

  public void setLikes(Integer likes) {
    this.likes = likes;
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

  public LocalDateTime getPostCreationTimeStamp() {
    return postCreationTimeStamp;
  }

  public void setPostCreationTimeStamp(LocalDateTime postCreationTimeStamp) {
    this.postCreationTimeStamp = postCreationTimeStamp;
  }

  public LocalDateTime getPostLastUpdateTimeStamp() {
    return postLastUpdateTimeStamp;
  }

  public void setPostLastUpdateTimeStamp(LocalDateTime postLastUpdateTimeStamp) {
    this.postLastUpdateTimeStamp = postLastUpdateTimeStamp;
  }


  public void setMedias(List<Media> Medias) {
    this.Media = Medias;
  }

  public User getPostByUser() {
    return postByUser;
  }

  public void setPostByUser(User postByUser) {
    this.postByUser = postByUser;
  }

  public List<Media> getMedia() {
    return Media;
  }

  public void addMedias(Media Media) {
    this.Media.add(Media);
  }

  public void removeMedias(Media Media) {
    this.Media.remove(Media);
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
