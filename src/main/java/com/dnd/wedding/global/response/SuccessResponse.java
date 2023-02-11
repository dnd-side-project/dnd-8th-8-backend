package com.dnd.wedding.global.response;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SuccessResponse {

  private final int status = 200;
  private String message = null;
  private Object data = null;

  public SuccessResponse(Object data) {
    this.data = data;
  }

  public SuccessResponse(String message, Object data) {
    this.message = message;
    this.data = data;
  }
}
