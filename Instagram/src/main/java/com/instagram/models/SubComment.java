package com.instagram.models;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;

@Entity
public class SubComment {

  @Id
  private UUID subCommentId;
  private String subComment;
  private Integer likes = 0;
  @CreationTimestamp
  private LocalDateTime subcommentCreationTimeStamp;
  @UpdateTimestamp
  private LocalDateTime subcommentLastUpdateTimeStamp;
  //user
  @ManyToOne
  private User subCommentByUser;

  @ManyToOne
  private Comment comment;

  protected SubComment() {
  }

  public SubComment(String subComment) {
    this.subCommentId = UUID.randomUUID();
    this.subComment = subComment;
    this.subcommentCreationTimeStamp = LocalDateTime.now();
    this.subcommentLastUpdateTimeStamp = LocalDateTime.now();
  }

  public UUID getSubCommentId() {
    return subCommentId;
  }

  public void setSubCommentId(UUID subCommentId) {
    this.subCommentId = subCommentId;
  }

  public String getSubComment() {
    return subComment;
  }

  public void setSubComment(String subComment) {
    this.subComment = subComment;
  }

  public Integer getLikes() {
    return likes;
  }

  public void setLikes(Integer likes) {
    this.likes = likes;
  }

  public LocalDateTime getSubcommentCreationTimeStamp() {
    return subcommentCreationTimeStamp;
  }

  public void setSubcommentCreationTimeStamp(LocalDateTime subcommentCreationTimeStamp) {
    this.subcommentCreationTimeStamp = subcommentCreationTimeStamp;
  }

  public LocalDateTime getSubcommentLastUpdateTimeStamp() {
    return subcommentLastUpdateTimeStamp;
  }

  public void setSubcommentLastUpdateTimeStamp(LocalDateTime subcommentLastUpdateTimeStamp) {
    this.subcommentLastUpdateTimeStamp = subcommentLastUpdateTimeStamp;
  }



  public Comment getComment() {
    return comment;
  }

  public void setComment(Comment comment) {
    this.comment = comment;
  }
}
