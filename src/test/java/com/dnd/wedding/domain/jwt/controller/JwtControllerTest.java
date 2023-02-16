package com.dnd.wedding.domain.jwt.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.cookies.CookieDocumentation.cookieWithName;
import static org.springframework.restdocs.cookies.CookieDocumentation.requestCookies;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
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
import org.springframework.test.web.servlet.ResultActions;

@WebMvcTest(JwtController.class)
class JwtControllerTest extends AbstractRestDocsTests {

  @MockBean
  JwtTokenProvider jwtTokenProvider;

  @MockBean
  JwtService jwtService;

  @Test
  @DisplayName("access token 갱신 성공 시 새로 발급한 token을 전달한다.")
  void refresh() throws Exception {

    // given
    given(jwtService.refreshToken(
        any(HttpServletRequest.class), any(HttpServletResponse.class), eq("accessToken")
    )).willReturn("newAccessToken");

    // when
    ResultActions result = mockMvc.perform(post("/api/v1/jwt/refresh")
            .cookie(new Cookie("refresh", "refreshToken"))
            .header("Authorization", "Bearer " + "accessToken"));

    // then
    result.andExpect(status().isOk())
        .andDo(document("jwt/refresh",
            requestCookies(
                cookieWithName("refresh").description("refresh token")
            ),
            requestHeaders(
                headerWithName("Authorization").description("access token")
            ),
            responseFields(
                fieldWithPath("status").description("응답 상태 코드"),
                fieldWithPath("message").description("응답 메시지"),
                fieldWithPath("data").description("응답 데이터"),
                fieldWithPath("data.accessToken").description("새로 발급한 access token")
            )
        ));
  }
}
