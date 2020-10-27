package com.instagram.Exceptions;

public class PasswordException extends Exception {

  public PasswordException() {
    super("Weak password!");
  }
}
