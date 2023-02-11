package com.dnd.wedding.global.exception;

import lombok.Getter;

@Getter
public class ErrorMessage {

  private int status;
  private String message;

  public ErrorMessage(int status, String message) {
    this.status = status;
    this.message = message;
  }
}
