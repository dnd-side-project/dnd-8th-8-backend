package com.dnd.weddingmap.global.response;

import lombok.Getter;

@Getter
public class ErrorResponse {

  private int status;
  private String message;

  public ErrorResponse(int status, String message) {
    this.status = status;
    this.message = message;
  }
}
