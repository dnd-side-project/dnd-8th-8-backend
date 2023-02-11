package com.dnd.wedding.global.exception;

import java.util.Date;
import lombok.Getter;

@Getter
public class ErrorMessage {

  private int statusCode;
  private Date timestamp;
  private String message;

  public ErrorMessage(int statusCode, Date timestamp, String message) {
    this.statusCode = statusCode;
    this.timestamp = timestamp;
    this.message = message;
  }
}
