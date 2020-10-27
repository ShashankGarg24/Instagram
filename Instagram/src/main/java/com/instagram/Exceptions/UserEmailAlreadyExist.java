package com.instagram.Exceptions;

import java.lang.reflect.Executable;

public class UserEmailAlreadyExist extends Exception{

  public UserEmailAlreadyExist(String userEmail){
    super(userEmail + " already exists!");
  }
}
