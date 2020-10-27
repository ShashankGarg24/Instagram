package com.instagram.Exceptions;

public class ConfirmPasswordDoNotMatch extends Exception{

  public ConfirmPasswordDoNotMatch(){
    super("Confirm password do not match!");
  }
}
