package com.instagram.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import javax.persistence.*;

@Entity
public class Comment {

  @Id
  private UUID commentId;
  private String comment;
  private Integer likes = 0;
  private Integer replyCount = 0;
  @CreationTimestamp
  private LocalDateTime commentCreationTimeStamp;
  @UpdateTimestamp
  private LocalDateTime commentLastUpdateTimeStamp;

  @OneToMany
  @JsonIgnore
  private List<LikeModel> userLikes = new ArrayList<>();

  @ManyToOne(fetch = FetchType.LAZY)
  @JsonIgnore
  private UserProfile parentUser;

  @OneToMany
  @JsonIgnore
  private List<Reply> replies = new ArrayList<>();

/*
  @OneToMany
  private List<SubComment> subComments;
*/

  protected Comment() {
  }

  public Comment(String comment, UserProfile parentUser) {
    this.commentId = UUID.randomUUID();
    this.comment = comment;
    this.commentCreationTimeStamp = LocalDateTime.now();
    this.commentLastUpdateTimeStamp = LocalDateTime.now();
    this.parentUser = parentUser;
  }

  public UUID getCommentId() {
    return commentId;
  }

  public void setCommentId(UUID commentId) {
    this.commentId = commentId;
  }

  public String getComment() {
    return comment;
  }

  public void setComment(String comment) {
    this.comment = comment;
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


  public Integer getReplyCount() {
    return replyCount;
  }

  public void increaseReplyCount() {
    ++this.replyCount;
  }

  public void decreaseReplyCount() {
    --this.replyCount;
  }

  public LocalDateTime getCommentCreationTimeStamp() {
    return commentCreationTimeStamp;
  }

  public void setCommentCreationTimeStamp(LocalDateTime commentCreationTimeStamp) {
    this.commentCreationTimeStamp = commentCreationTimeStamp;
  }

  public LocalDateTime getCommentLastUpdateTimeStamp() {
    return commentLastUpdateTimeStamp;
  }

  public void setCommentLastUpdateTimeStamp(LocalDateTime commentLastUpdateTimeStamp) {
    this.commentLastUpdateTimeStamp = commentLastUpdateTimeStamp;
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

  public List<Reply> getReplies() {
    return replies;
  }

  public void addToReplies(Reply reply) {
    this.replies.add(reply);
  }

  public void removeFromReplies(Reply reply) {
    this.replies.remove(reply);
  }

}
