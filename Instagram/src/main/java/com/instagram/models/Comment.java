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
  @CreationTimestamp
  private LocalDateTime commentCreationTimeStamp;
  @UpdateTimestamp
  private LocalDateTime commentLastUpdateTimeStamp;

  @OneToMany
  @JsonIgnore
  private List<LikeModel> userLikes = new ArrayList<>();

/*  @ManyToOne
  private Posts post;
//user
  @ManyToOne
  private User commentByUser;

  @OneToMany
  private List<SubComment> subComments;
*/

  protected Comment() {
  }

  public Comment(String comment, LocalDateTime commentCreationTimeStamp, LocalDateTime commentLastUpdateTimeStamp) {
    this.commentId = UUID.randomUUID();
    this.comment = comment;
    this.commentCreationTimeStamp = LocalDateTime.now();
    this.commentLastUpdateTimeStamp = LocalDateTime.now();
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

  public void setLikes(Integer likes) {
    this.likes = likes;
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

 /* public Posts getPost() {
    return post;
  }

  public void setPost(Posts post) {
    this.post = post;
  }

  public User getCommentByUser() {
    return commentByUser;
  }

  public void setCommentByUser(User commentByUser) {
    this.commentByUser = commentByUser;
  }

  public List<SubComment> getSubComments() {
    return subComments;
  }

  public void addSubComments(SubComment subComment) {
    this.subComments.add(subComment);
  }

  public void removeSubComments(SubComment subComment) {
    this.subComments.remove(subComment);
  }
*/
}
