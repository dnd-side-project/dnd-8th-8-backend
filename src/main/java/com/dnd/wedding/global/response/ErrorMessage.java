package com.dnd.wedding.global.response;

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
