package com.instagram.Exceptions;

public class EmptyField extends Exception {

  public EmptyField() {
    super("Field can't be empty!");
  }
}
