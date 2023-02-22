package com.dnd.weddingmap.global.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR, reason = "InternalServerError")
public class InternalServerErrorException extends RuntimeException {

  public InternalServerErrorException(String message) {
    super(message);
  }
}
