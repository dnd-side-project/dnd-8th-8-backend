package com.dnd.weddingmap.global.response;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

class SuccessResponseTest {

  @Test
  @DisplayName("상태코드 확인")
  void checkCode() {
    SuccessResponse successResponse = new SuccessResponse();
    assertEquals(200, successResponse.getStatus());
  }

  @Test
  @DisplayName("메시지 확인")
  void checkMessage() {
    SuccessResponse successResponse = SuccessResponse.builder().message("message").build();
    assertEquals(200, successResponse.getStatus());
    assertEquals("message", successResponse.getMessage());
  }

  @Test
  @DisplayName("데이터 확인")
  void checkData() {
    SuccessResponse successResponse = SuccessResponse.builder().data("test").build();
    assertEquals(200, successResponse.getStatus());
    assertEquals("test", successResponse.getData());
  }

  @Test
  @DisplayName("데이터 및 메시지 확인")
  void checkDataAndMessage() {
    SuccessResponse successResponse = new SuccessResponse(HttpStatus.OK, "success", "test");
    assertEquals(200, successResponse.getStatus());
    assertEquals("success", successResponse.getMessage());
    assertEquals("test", successResponse.getData());
  }
}
