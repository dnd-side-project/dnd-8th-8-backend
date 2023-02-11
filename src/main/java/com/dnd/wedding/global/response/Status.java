package com.dnd.wedding.global.response;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum Status {
  SUCCESS(HttpStatus.OK, "성공"),
  BAD_REQUEST(HttpStatus.BAD_REQUEST, "잘못된 요청"),
  UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "인증되지 않은 사용자"),
  FORBIDDEN(HttpStatus.FORBIDDEN, "접근 권한 없음"),
  NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 리소스"),
  INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버 내부 오류");

  private final HttpStatus httpStatus;
  private final String message;

  Status(HttpStatus httpStatus, String message) {
    this.httpStatus = httpStatus;
    this.message = message;
  }
}
