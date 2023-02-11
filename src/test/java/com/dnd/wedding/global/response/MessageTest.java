package com.dnd.wedding.global.response;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class MessageTest {

  @Test
  @DisplayName("성공 메시지 생성")
  void successResponse() {
    Message message = new Message(Status.SUCCESS, "test");
    assertEquals(message.getStatus(), Status.SUCCESS);
    assertEquals(message.getMessage(), Status.SUCCESS.getMessage());
    assertEquals(message.getData(), "test");
  }

  @Test
  @DisplayName("잘못된 요청 메시지 생성")
  void badRequestResponse() {
    Message message = new Message(Status.BAD_REQUEST);
    assertEquals(message.getStatus(), Status.BAD_REQUEST);
    assertEquals(message.getMessage(), Status.BAD_REQUEST.getMessage());
    assertNull(message.getData());
  }

  @Test
  @DisplayName("인증되지 않은 사용자 메시지 생성")
  void unauthorizedResponse() {
    Message message = new Message(Status.UNAUTHORIZED);
    assertEquals(message.getStatus(), Status.UNAUTHORIZED);
    assertEquals(message.getMessage(), Status.UNAUTHORIZED.getMessage());
    assertNull(message.getData());
  }

  @Test
  @DisplayName("접근 권한 없음 메시지 생성")
  void forbiddenResponse() {
    Message message = new Message(Status.FORBIDDEN);
    assertEquals(message.getStatus(), Status.FORBIDDEN);
    assertEquals(message.getMessage(), Status.FORBIDDEN.getMessage());
    assertNull(message.getData());
  }

  @Test
  @DisplayName("존재하지 않는 리소스 메시지 생성")
  void notFoundResponse() {
    Message message = new Message(Status.NOT_FOUND);
    assertEquals(message.getStatus(), Status.NOT_FOUND);
    assertEquals(message.getMessage(), Status.NOT_FOUND.getMessage());
    assertNull(message.getData());
  }

  @Test
  @DisplayName("서버 내부 오류 메시지 생성")
  void internalServerErrorResponse() {
    Message message = new Message(Status.INTERNAL_SERVER_ERROR);
    assertEquals(message.getStatus(), Status.INTERNAL_SERVER_ERROR);
    assertEquals(message.getMessage(), Status.INTERNAL_SERVER_ERROR.getMessage());
    assertNull(message.getData());
  }
}