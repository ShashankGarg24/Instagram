package com.instagram.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import javax.persistence.*;

@Entity
public class Reply {

  @Id
  private UUID replyId;
  private String reply;
  private Integer likes = 0;
  @CreationTimestamp
  private LocalDateTime replyCreationTimeStamp;
  @UpdateTimestamp
  private LocalDateTime replyLastUpdateTimeStamp;


  @OneToMany
  @JsonIgnore
  private List<LikeModel> userLikes = new ArrayList<>();

  @ManyToOne(fetch = FetchType.LAZY)
  @JsonIgnore
  private UserProfile parentUser;

  protected Reply() {
  }

  public Reply(String reply, UserProfile parentUser) {
    this.replyId = UUID.randomUUID();
    this.reply = reply;
    this.replyCreationTimeStamp = LocalDateTime.now();
    this.replyLastUpdateTimeStamp = LocalDateTime.now();
    this.parentUser = parentUser;
  }

  public UUID getReplyId() {
    return replyId;
  }

  public void setReplyId(UUID replyId) {
    this.replyId = replyId;
  }

  public String getReply() {
    return reply;
  }

  public void setReply(String reply) {
    this.reply = reply;
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

  public LocalDateTime getReplyCreationTimeStamp() {
    return replyCreationTimeStamp;
  }

  public void setReplyCreationTimeStamp(LocalDateTime replyCreationTimeStamp) {
    this.replyCreationTimeStamp = replyCreationTimeStamp;
  }

  public LocalDateTime getReplyLastUpdateTimeStamp() {
    return replyLastUpdateTimeStamp;
  }

  public void setReplyLastUpdateTimeStamp(LocalDateTime replyLastUpdateTimeStamp) {
    this.replyLastUpdateTimeStamp = replyLastUpdateTimeStamp;
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

  public UserProfile getParentUser() {
    return parentUser;
  }

  public void setParentUser(UserProfile parentUser) {
    this.parentUser = parentUser;
  }
}
