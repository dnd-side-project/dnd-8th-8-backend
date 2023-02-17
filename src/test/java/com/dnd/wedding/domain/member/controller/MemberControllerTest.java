package com.dnd.wedding.domain.member.controller;


import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.dnd.wedding.docs.springrestdocs.AbstractRestDocsTests;
import com.dnd.wedding.domain.jwt.JwtTokenProvider;
import com.dnd.wedding.domain.member.Gender;
import com.dnd.wedding.domain.member.service.MemberService;
import com.dnd.wedding.global.WithMockOAuth2User;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.test.web.servlet.ResultActions;

@WebMvcTest(MemberController.class)
class MemberControllerTest extends AbstractRestDocsTests {

  @MockBean
  JwtTokenProvider jwtTokenProvider;

  @MockBean
  MemberService memberService;

  @Test
  @DisplayName("성별 정보 조회 테스트")
  @WithMockOAuth2User
  void getGender() throws Exception {

    // given
    given(memberService.getGender(anyLong()))
        .willReturn(Optional.of(Gender.MALE));

    // when
    ResultActions result = mockMvc.perform(get("/api/v1/user/gender")
        .header(HttpHeaders.AUTHORIZATION, "Bearer " + "accessToken"));

    // then
    result.andExpect(status().isOk())
        .andDo(document("member/get-gender",
            requestHeaders(
                headerWithName(HttpHeaders.AUTHORIZATION).description("액세스 토큰")
            ),
            responseFields(
                fieldWithPath("status").description("응답 상태 코드"),
                fieldWithPath("message").description("응답 메시지"),
                fieldWithPath("data.gender").description("성별 정보")
            )
        ));
  }

  @Test
  @DisplayName("프로필 이미지 조회 테스트")
  @WithMockOAuth2User
  void getProfileImage() throws Exception {

    // given
    given(memberService.getProfileImage(anyLong()))
        .willReturn(Optional.of("https://image.storage.com/profile/1"));

    // when
    ResultActions result = mockMvc.perform(get("/api/v1/user/profile")
        .header(HttpHeaders.AUTHORIZATION, "Bearer " + "accessToken"));

    // then
    result.andExpect(status().isOk())
        .andDo(document("member/get-profile",
            requestHeaders(
                headerWithName(HttpHeaders.AUTHORIZATION).description("액세스 토큰")
            ),
            responseFields(
                fieldWithPath("status").description("응답 상태 코드"),
                fieldWithPath("message").description("응답 메시지"),
                fieldWithPath("data.url").description("프로필 이미지 URL")
            )
        ));
  }
}
