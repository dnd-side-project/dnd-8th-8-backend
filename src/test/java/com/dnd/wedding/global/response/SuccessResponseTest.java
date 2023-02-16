package com.dnd.wedding.global.response;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

class SuccessResponseTest {

  @Test
  @DisplayName("상태코드 확인")
  void checkCode() {
    SuccessResponse successResponse = new SuccessResponse();
    assertEquals(successResponse.getStatus(), 200);
  }

  @Test
  @DisplayName("메시지 확인")
  void checkMessage() {
    SuccessResponse successResponse = SuccessResponse.builder().message("message").build();
    assertEquals(successResponse.getStatus(), 200);
    assertEquals(successResponse.getMessage(), "message");
  }

  @Test
  @DisplayName("데이터 확인")
  void checkData() {
    SuccessResponse successResponse = SuccessResponse.builder().data("test").build();
    assertEquals(successResponse.getStatus(), 200);
    assertEquals(successResponse.getData(), "test");
  }

  @Test
  @DisplayName("데이터 및 메시지 확인")
  void checkDataAndMessage() {
    SuccessResponse successResponse = new SuccessResponse(HttpStatus.OK, "success", "test");
    assertEquals(successResponse.getStatus(), 200);
    assertEquals(successResponse.getMessage(), "success");
    assertEquals(successResponse.getData(), "test");
  }
}
