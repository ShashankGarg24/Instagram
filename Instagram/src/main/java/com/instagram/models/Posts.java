package com.instagram.models;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.ManyToAny;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.context.annotation.Lazy;

import java.time.LocalDateTime;
import java.util.ArrayList;
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

  @OneToMany(fetch = FetchType.LAZY)
  private List<Media> media = new ArrayList<>();

  //tagged users

 /* @OneToMany
  private List<Comment> comments;
*/

  public Posts() {

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


  public void setPostLastUpdateTimeStamp(LocalDateTime postLastUpdateTimeStamp) {
    this.postLastUpdateTimeStamp = postLastUpdateTimeStamp;
  }


  public List<Media> getMedia() {
    return media;
  }

  public void addMedia(Media media) {
    this.media.add(media);
  }

  public void removeMedia(Media Media) {
    this.media.remove(Media);
  }

 /* public List<Comment> getComments() {
    return comments;
  }

  public void addComment(Comment comment) {
    this.comments.add(comment);
  }

  public void removeComment(Comment comment) {
    this.comments.remove(comment);
  }*/
}
