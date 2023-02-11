package com.dnd.wedding.global.response;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class SuccessResponseTest {

  @Test
  @DisplayName("상태코드 확인")
  void checkCode() {
    SuccessResponse successResponse = new SuccessResponse();
    assertEquals(successResponse.getStatus(), 200);
  }

  @Test
  @DisplayName("데이터 확인")
  void checkData() {
    SuccessResponse successResponse = new SuccessResponse("test");
    assertEquals(successResponse.getStatus(), 200);
    assertEquals(successResponse.getData(), "test");
  }

  @Test
  @DisplayName("데이터 및 메시지 확인")
  void checkDataAndMessage() {
    SuccessResponse successResponse = new SuccessResponse("success", "test");
    assertEquals(successResponse.getStatus(), 200);
    assertEquals(successResponse.getMessage(), "success");
    assertEquals(successResponse.getData(), "test");
  }
}
