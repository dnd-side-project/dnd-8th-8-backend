package com.dnd.wedding.global.response;

import lombok.Builder;
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

  @Builder
  public SuccessResponse(String message, Object data) {
    this.message = message;
    this.data = data;
  }
}
