package com.dnd.wedding.domain.jwt.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.cookies.CookieDocumentation.cookieWithName;
import static org.springframework.restdocs.cookies.CookieDocumentation.requestCookies;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.queryParameters;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.dnd.wedding.docs.springrestdocs.AbstractRestDocsTests;
import com.dnd.wedding.domain.jwt.JwtTokenProvider;
import com.dnd.wedding.domain.jwt.service.JwtService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@WebMvcTest(JwtController.class)
class JwtControllerTest extends AbstractRestDocsTests {

  @MockBean
  JwtTokenProvider jwtTokenProvider;

  @MockBean
  JwtService jwtService;

  @Test
  @DisplayName("access token 갱신 성공 시 새로 발급한 token을 전달한다.")
  void refresh() throws Exception {

    Cookie cookie = new Cookie("refresh", "refreshToken");
    given(jwtService.refreshToken(
        any(HttpServletRequest.class), any(HttpServletResponse.class), eq("accessToken")
    )).willReturn("newAccessToken");

    mockMvc.perform(get("/api/v1/jwt/refresh?accessToken=accessToken").cookie(cookie))
        .andExpect(status().isOk())
        .andDo(document("jwt/refresh",
            requestCookies(
                cookieWithName("refresh").description("refresh token")
            ),
            queryParameters(
                parameterWithName("accessToken").description("갱신할 access token")
            ),
            responseFields(
                fieldWithPath("status").description("응답 상태 코드"),
                fieldWithPath("message").description("응답 메시지"),
                fieldWithPath("data").description("응답 데이터")
            )
        ));
  }
}
