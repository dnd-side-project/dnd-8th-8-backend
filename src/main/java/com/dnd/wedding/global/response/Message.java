package com.dnd.wedding.global.response;

import lombok.Data;
import lombok.Getter;

@Getter
public class Message {

  private Status status;
  private String message;
  private Object data = null;

  public Message(Status status) {
    this.status = status;
    this.message = status.getMessage();
  }

  public Message(Status status, Object data) {
    this.status = status;
    this.message = status.getMessage();
    this.data = data;
  }
}
