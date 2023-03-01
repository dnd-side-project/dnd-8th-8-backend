package com.dnd.weddingmap.domain.member.controller;


import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.delete;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.multipart;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.put;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.partWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParts;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.dnd.weddingmap.docs.springrestdocs.AbstractRestDocsTests;
import com.dnd.weddingmap.domain.jwt.JwtTokenProvider;
import com.dnd.weddingmap.domain.member.Gender;
import com.dnd.weddingmap.domain.member.dto.GenderDto;
import com.dnd.weddingmap.domain.member.dto.NameDto;
import com.dnd.weddingmap.domain.member.service.MemberService;
import com.dnd.weddingmap.global.WithMockOAuth2User;
import com.dnd.weddingmap.global.service.S3Service;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.web.multipart.MultipartFile;

@WebMvcTest(MemberController.class)
class MemberControllerTest extends AbstractRestDocsTests {

  static final String ACCESS_TOKEN_PREFIX = "Bearer ";

  @MockBean
  JwtTokenProvider jwtTokenProvider;

  @MockBean
  MemberService memberService;

  @MockBean
  S3Service s3Service;

  @Autowired
  ObjectMapper objectMapper;

  @Test
  @DisplayName("사용자 이름 변경")
  @WithMockOAuth2User
  void modifyName() throws Exception {

    // given
    NameDto dto = NameDto.builder()
        .name("홍길동").build();

    // when
    ResultActions result = mockMvc.perform(put("/api/v1/user/name")
        .header(HttpHeaders.AUTHORIZATION, ACCESS_TOKEN_PREFIX + "ACCESS_TOKEN")
        .content(objectMapper.writeValueAsString(dto))
        .contentType(MediaType.APPLICATION_JSON)
    );

    // then
    result.andExpect(status().isOk())
        .andDo(document("member/put-name",
            requestHeaders(
                headerWithName(HttpHeaders.AUTHORIZATION).description("액세스 토큰")
            ),
            requestFields(
                fieldWithPath("name").description("사용자 이름")
            ),
            responseFields(
                fieldWithPath("status").description("응답 상태 코드"),
                fieldWithPath("message").description("응답 메시지")
            )
        ));
  }

  @Test
  @DisplayName("성별 정보 조회 테스트")
  @WithMockOAuth2User
  void getGender() throws Exception {

    // given
    given(memberService.getGender(anyLong()))
        .willReturn(Optional.of(Gender.MALE));

    // when
    ResultActions result = mockMvc.perform(get("/api/v1/user/gender")
        .header(HttpHeaders.AUTHORIZATION, ACCESS_TOKEN_PREFIX + "ACCESS_TOKEN"));

    // then
    result.andExpect(status().isOk())
        .andDo(document("member/get-gender",
            requestHeaders(
                headerWithName(HttpHeaders.AUTHORIZATION).description("액세스 토큰")
            ),
            responseFields(
                fieldWithPath("status").description("응답 상태 코드"),
                fieldWithPath("data.gender").description("성별 정보")
            )
        ));
  }

  @Test
  @DisplayName("성별 정보 추가 테스트")
  @WithMockOAuth2User
  void postGender() throws Exception {

    // given
    GenderDto dto = GenderDto.builder()
        .gender(Gender.MALE).build();

    // when
    ResultActions result = mockMvc.perform(post("/api/v1/user/gender")
        .header(HttpHeaders.AUTHORIZATION, ACCESS_TOKEN_PREFIX + "ACCESS_TOKEN")
        .content(objectMapper.writeValueAsString(dto))
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON)
    );

    // then
    result.andExpect(status().isOk())
        .andDo(document("member/post-gender",
            requestHeaders(
                headerWithName(HttpHeaders.AUTHORIZATION).description("액세스 토큰")
            ),
            requestFields(
                fieldWithPath("gender").description("성별 정보")
            ),
            responseFields(
                fieldWithPath("status").description("응답 상태 코드"),
                fieldWithPath("message").description("응답 메시지")
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
        .header(HttpHeaders.AUTHORIZATION, ACCESS_TOKEN_PREFIX + "ACCESS_TOKEN"));

    // then
    result.andExpect(status().isOk())
        .andDo(document("member/get-profile",
            requestHeaders(
                headerWithName(HttpHeaders.AUTHORIZATION).description("액세스 토큰")
            ),
            responseFields(
                fieldWithPath("status").description("응답 상태 코드"),
                fieldWithPath("data.url").description("프로필 이미지 URL")
            )
        ));
  }

  @Test
  @DisplayName("프로필 이미지 수정 테스트")
  @WithMockOAuth2User
  void postProfile() throws Exception {

    // given
    MockMultipartFile image = new MockMultipartFile(
        "image", "image.png", "image/png", "image data".getBytes());
    given(s3Service.upload(any(MultipartFile.class), eq("profile")))
        .willReturn("https://image.storage.com/profile/1");

    // when
    ResultActions result = mockMvc.perform(multipart("/api/v1/user/profile")
        .file(image)
        .header(HttpHeaders.AUTHORIZATION, ACCESS_TOKEN_PREFIX + "ACCESS_TOKEN")
        .contentType(MediaType.MULTIPART_FORM_DATA)
        .accept(MediaType.APPLICATION_JSON)
    );

    // then
    result.andExpect(status().isOk())
        .andDo(document("member/put-profile",
            requestHeaders(
                headerWithName(HttpHeaders.AUTHORIZATION).description("액세스 토큰")
            ),
            requestParts(
                partWithName("image").description("프로필 이미지")
            ),
            responseFields(
                fieldWithPath("status").description("응답 상태 코드"),
                fieldWithPath("data.url").description("사용자 프로필 이미지 URL")
            )
        ));
  }

  @Test
  @DisplayName("사용자 탈퇴 테스트")
  @WithMockOAuth2User
  void deleteMember() throws Exception {

    // given
    given(memberService.withdraw(anyLong())).willReturn(true);

    // when
    ResultActions result = mockMvc.perform(delete("/api/v1/user")
        .header(HttpHeaders.AUTHORIZATION, "Bearer " + "ACCESS_TOKEN"));

    // then
    result.andExpect(status().isOk())
        .andDo(document("member/delete",
            requestHeaders(
                headerWithName(HttpHeaders.AUTHORIZATION).description("액세스 토큰")
            ),
            responseFields(
                fieldWithPath("status").description("응답 상태 코드"),
                fieldWithPath("message").description("응답 메시지")
            )
        ));
  }
}
