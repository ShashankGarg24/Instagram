package com.instagram.models;

import java.util.UUID;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Posts {

  @Id
  private UUID postId;
  private Integer likes;
  private String date;
  private String location;
  //images
  //videos
  //user
  //tagged users
  //comments


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

  public String getDate() {
    return date;
  }

  public void setDate(String date) {
    this.date = date;
  }

  public String getLocation() {
    return location;
  }

  public void setLocation(String location) {
    this.location = location;
  }
}
