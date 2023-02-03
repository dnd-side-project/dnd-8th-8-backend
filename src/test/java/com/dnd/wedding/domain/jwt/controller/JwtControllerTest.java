package com.dnd.wedding.domain.jwt.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

import com.dnd.wedding.domain.jwt.service.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

class JwtControllerTest {

  JwtController jwtController;
  JwtService jwtService;
  MockHttpServletRequest request;
  MockHttpServletResponse response;

  @BeforeEach
  void init() {
    jwtService = mock(JwtService.class);
    jwtController = new JwtController(jwtService);
    request = new MockHttpServletRequest();
    response = new MockHttpServletResponse();
  }

  @Test
  @DisplayName("access token 갱신 성공 시 새로 발급한 token을 전달한다.")
  void successRenewAccessToken() {
    // given
    given(jwtService.refreshToken(request, response, "accessToken")).willReturn("token");

    // when
    ResponseEntity responseEntity = jwtController.refreshToken(request, response, "accessToken");

    // then
    assertEquals("token", responseEntity.getBody());
    assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
  }
}
