package com.instagram.Exceptions;

public class UsernameAlreadyExist extends Exception{

  public UsernameAlreadyExist(String username){
    super(username + " already exists!");
  }
}
