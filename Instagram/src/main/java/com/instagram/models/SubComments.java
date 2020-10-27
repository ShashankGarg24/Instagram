package com.instagram.models;

import java.util.Date;
import java.util.UUID;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class SubComments {

  @Id
  private UUID subCommentId;
  private String subComment;
  private Integer likes;
  private Date date;
  //user
}
