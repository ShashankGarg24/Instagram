package com.instagram.models;

import java.util.Date;
import java.util.UUID;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Comments {

  @Id
  private UUID commentId;
  private String comment;
  private Integer likes;
  private Date date;
  //user
  //subcomments

}
