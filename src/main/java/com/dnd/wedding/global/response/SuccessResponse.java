package com.dnd.wedding.global.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@NoArgsConstructor
public class SuccessResponse {

  private int status = 200;
  private String message = null;
  private Object data = null;

  @Builder
  public SuccessResponse(HttpStatus httpStatus, String message, Object data) {
    this.status = httpStatus == null ? 200 : httpStatus.value();
    this.message = message;
    this.data = data;
  }
}
