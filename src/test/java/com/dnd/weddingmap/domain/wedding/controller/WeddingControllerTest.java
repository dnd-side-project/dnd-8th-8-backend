package com.dnd.weddingmap.domain.wedding.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.put;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.dnd.weddingmap.docs.springrestdocs.AbstractRestDocsTests;
import com.dnd.weddingmap.domain.jwt.JwtTokenProvider;
import com.dnd.weddingmap.domain.wedding.dto.BudgetDto;
import com.dnd.weddingmap.domain.wedding.dto.WeddingDayDto;
import com.dnd.weddingmap.domain.wedding.service.WeddingService;
import com.dnd.weddingmap.global.WithMockOAuth2User;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

@WebMvcTest(WeddingController.class)
class WeddingControllerTest extends AbstractRestDocsTests {

  static final String ACCESS_TOKEN_PREFIX = "Bearer ";

  @MockBean
  JwtTokenProvider jwtTokenProvider;

  @MockBean
  WeddingService weddingService;

  @Autowired
  ObjectMapper objectMapper;

  @Test
  @DisplayName("결혼 등록 테스트")
  @WithMockOAuth2User
  void registerWedding() throws Exception {
    // given
    given(weddingService.registerWedding(anyLong(), any(WeddingDayDto.class))).willReturn(1L);
    WeddingDayDto weddingDayDto = WeddingDayDto.builder()
        .weddingDay(LocalDate.parse("2033-03-25")).build();

    // when
    ResultActions result = mockMvc.perform(
        post("/api/v1/wedding").contentType(MediaType.APPLICATION_JSON)
            .header(HttpHeaders.AUTHORIZATION, ACCESS_TOKEN_PREFIX + "ACCESS_TOKEN")
            .content(objectMapper.writeValueAsString(weddingDayDto)));

    // then
    result.andExpect(status().isCreated()).andDo(document("wedding/register-wedding",
        requestHeaders(headerWithName(HttpHeaders.AUTHORIZATION).description("액세스 토큰")),
        requestFields(fieldWithPath("weddingDay").description("결혼일")),
        responseFields(fieldWithPath("status").description("응답 상태 코드"),
            fieldWithPath("message").description("응답 메시지"),
            fieldWithPath("data").description("결혼 ID"))));
  }

  @Test
  @DisplayName("결혼일 조회 테스트")
  @WithMockOAuth2User
  void getWeddingDay() throws Exception {
    // given
    given(weddingService.getWeddingDay(any())).willReturn(
        WeddingDayDto.builder().weddingDay(LocalDate.parse("2033-03-25")).build());

    // when
    ResultActions result = mockMvc.perform(
        get("/api/v1/wedding/day").contentType(MediaType.APPLICATION_JSON)
            .header(HttpHeaders.AUTHORIZATION, ACCESS_TOKEN_PREFIX + "ACCESS_TOKEN"));

    // then
    result.andExpect(status().isOk()).andDo(document("wedding/get-wedding-day",
        requestHeaders(headerWithName(HttpHeaders.AUTHORIZATION).description("액세스 토큰")),
        responseFields(fieldWithPath("status").description("응답 상태 코드"),
            fieldWithPath("message").description("응답 메시지"),
            fieldWithPath("data.weddingDay").description("결혼일"))));
  }

  @Test
  @DisplayName("결혼일 수정 테스트")
  @WithMockOAuth2User
  void modifyWeddingDay() throws Exception {
    // given
    WeddingDayDto weddingDayDto = WeddingDayDto.builder()
        .weddingDay(LocalDate.parse("2033-03-25")).build();

    // when
    ResultActions result = mockMvc.perform(
        put("/api/v1/wedding/day").contentType(MediaType.APPLICATION_JSON)
            .header(HttpHeaders.AUTHORIZATION, ACCESS_TOKEN_PREFIX + "ACCESS_TOKEN")
            .content(objectMapper.writeValueAsString(weddingDayDto)));

    // then
    result.andExpect(status().isOk()).andDo(document("wedding/modify-wedding-day",
        requestHeaders(headerWithName(HttpHeaders.AUTHORIZATION).description("액세스 토큰")),
        requestFields(fieldWithPath("weddingDay").description("결혼일")),
        responseFields(fieldWithPath("status").description("응답 상태 코드"),
            fieldWithPath("message").description("응답 메시지"),
            fieldWithPath("data").description("데이터").ignored())));
  }

  @Test
  @DisplayName("결혼 예산 조회 테스트")
  @WithMockOAuth2User
  void getBudget() throws Exception {
    // given
    given(weddingService.getBudget(any())).willReturn(new BudgetDto(1000000L));

    // when
    ResultActions result = mockMvc.perform(
        get("/api/v1/wedding/budget").contentType(MediaType.APPLICATION_JSON)
            .header(HttpHeaders.AUTHORIZATION, ACCESS_TOKEN_PREFIX + "ACCESS_TOKEN"));

    // then
    result.andExpect(status().isOk()).andDo(document("wedding/get-budget",
        requestHeaders(headerWithName(HttpHeaders.AUTHORIZATION).description("액세스 토큰")),
        responseFields(fieldWithPath("status").description("응답 상태 코드"),
            fieldWithPath("message").description("응답 메시지"),
            fieldWithPath("data.budget").description("결혼 예산"))));
  }

  @Test
  @DisplayName("결혼 예산 수정 테스트")
  @WithMockOAuth2User
  void modifyBudget() throws Exception {
    // given
    BudgetDto budgetDto = new BudgetDto(1000000L);

    // when
    ResultActions result = mockMvc.perform(
        put("/api/v1/wedding/budget").contentType(MediaType.APPLICATION_JSON)
            .header(HttpHeaders.AUTHORIZATION, ACCESS_TOKEN_PREFIX + "ACCESS_TOKEN")
            .content(objectMapper.writeValueAsString(budgetDto)));

    // then
    result.andExpect(status().isOk()).andDo(document("wedding/modify-budget",
        requestHeaders(headerWithName(HttpHeaders.AUTHORIZATION).description("액세스 토큰")),
        requestFields(fieldWithPath("budget").description("결혼 예산")),
        responseFields(fieldWithPath("status").description("응답 상태 코드"),
            fieldWithPath("message").description("응답 메시지"),
            fieldWithPath("data").description("데이터").ignored())));
  }
}
