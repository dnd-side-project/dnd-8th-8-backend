package com.dnd.weddingmap.global.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.REQUEST_TIMEOUT)
public class RequestTimeoutException extends RuntimeException {

  public RequestTimeoutException(String message) {
    super(message);
  }
}
