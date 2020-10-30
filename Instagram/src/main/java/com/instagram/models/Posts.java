package com.instagram.models;

import java.util.Date;
import java.util.UUID;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.xml.crypto.Data;

@Entity
public class Posts {

  @Id
  private UUID postId;
  private Integer likes = 0;
  private Date date;
  private String location;
  private String caption;
  private boolean commentActivity;
  //images
  //videos
  //user
  //tagged users
  //comments


  public Posts( String location, String caption, boolean commentActivity) {
    this.postId = UUID.randomUUID();
    this.date = new Date();
    this.location = location;
    this.caption = caption;
    this.commentActivity = commentActivity;
  }

  public Posts() {

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

  public Date getDate() {
    return date;
  }

  public void setDate(Date date) {
    this.date = date;
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
}
