package com.dnd.wedding.global.exception;

import lombok.Getter;

@Getter
public class ErrorMessage {

  private int statusCode;
  private String message;

  public ErrorMessage(int statusCode, String message) {
    this.statusCode = statusCode;
    this.message = message;
  }
}
