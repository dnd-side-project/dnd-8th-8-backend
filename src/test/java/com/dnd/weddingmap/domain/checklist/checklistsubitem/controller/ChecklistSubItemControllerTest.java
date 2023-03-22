package com.dnd.weddingmap.domain.checklist.checklistsubitem.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.delete;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.put;
import static org.springframework.restdocs.payload.PayloadDocumentation.beneathPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.dnd.weddingmap.docs.springrestdocs.AbstractRestDocsTests;
import com.dnd.weddingmap.domain.checklist.checklistitem.ChecklistItem;
import com.dnd.weddingmap.domain.checklist.checklistitem.service.ChecklistItemService;
import com.dnd.weddingmap.domain.checklist.checklistsubitem.ChecklistSubItem;
import com.dnd.weddingmap.domain.checklist.checklistsubitem.dto.ChecklistSubItemDto;
import com.dnd.weddingmap.domain.checklist.checklistsubitem.dto.ChecklistSubItemStateDto;
import com.dnd.weddingmap.domain.checklist.checklistsubitem.service.ChecklistSubItemService;
import com.dnd.weddingmap.domain.jwt.JwtTokenProvider;
import com.dnd.weddingmap.domain.member.Member;
import com.dnd.weddingmap.domain.member.Role;
import com.dnd.weddingmap.domain.member.service.MemberService;
import com.dnd.weddingmap.domain.oauth.OAuth2Provider;
import com.dnd.weddingmap.global.WithMockOAuth2User;
import com.dnd.weddingmap.global.util.MessageUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.time.LocalDate;
import java.time.LocalTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.ResultActions;

@WebMvcTest(ChecklistSubItemController.class)
class ChecklistSubItemControllerTest extends AbstractRestDocsTests {

  static final Long CHECKLIST_ITEM_ID = 1L;
  static final Long CHECKLIST_SUB_ITEM_ID = 1L;
  static final String ACCESS_TOKEN_PREFIX = "Bearer ";

  @MockBean
  ChecklistItemService checklistItemService;
  @MockBean
  ChecklistSubItemService checklistSubItemService;
  @MockBean
  MemberService memberService;
  @MockBean
  JwtTokenProvider jwtTokenProvider;

  @Autowired
  MessageSource messageSource;
  private ObjectMapper objectMapper;

  private final Member member = Member.builder()
      .id(1L)
      .name("test")
      .email("test@example.com")
      .profileImage("test.png")
      .role(Role.USER)
      .oauth2Provider(OAuth2Provider.GOOGLE)
      .build();

  private final ChecklistItem checklistItem = ChecklistItem.builder()
      .id(1L)
      .title("title")
      .checkDate(LocalDate.now())
      .startTime(LocalTime.MIN)
      .endTime(LocalTime.MAX)
      .place("place")
      .memo("memo")
      .member(member)
      .build();

  private final ChecklistSubItem checklistSubItem = ChecklistSubItem.builder()
      .id(1L)
      .contents("contents")
      .isChecked(false)
      .build();

  @BeforeEach
  void init() {
    objectMapper = new ObjectMapper();
    objectMapper.registerModule(new JavaTimeModule());
    MessageUtil.setMessageSource(messageSource);
  }

  @Test
  @WithMockOAuth2User
  @DisplayName("체크리스트 서브 아이템 생성")
  void createChecklistSubItem() throws Exception {
    String url = "/api/v1/checklist/item/{item-id}/sub-item";

    ChecklistSubItemDto createChecklistSubItemDto = ChecklistSubItemDto.builder()
        .contents("contents")
        .build();

    // given
    given(checklistItemService.findChecklistItem(anyLong(), anyLong())).willReturn(checklistItem);
    given(checklistSubItemService.saveChecklistSubItem(any(ChecklistSubItemDto.class),
        any(ChecklistItem.class))).willReturn(checklistSubItem);

    // when
    ResultActions result = mockMvc.perform(post(url, CHECKLIST_ITEM_ID)
        .header(HttpHeaders.AUTHORIZATION, ACCESS_TOKEN_PREFIX + "ACCESS_TOKEN")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(createChecklistSubItemDto)));

    // then
    result.andExpect(status().isCreated())
        .andDo(
            document("checklist/sub-item/checklist-sub-item-create",
                pathParameters(
                    parameterWithName("item-id").description("체크리스트 아이템 아이디")
                ), requestFields(
                    fieldWithPath("id").ignored(),
                    fieldWithPath("contents").description("등록할 체크리스트 서브 아이템 내용 (* required)").type(
                        JsonFieldType.STRING),
                    fieldWithPath("isChecked").ignored()
                ),
                responseFields(
                    beneathPath("data").withSubsectionId("data"),
                    fieldWithPath("id").description("등록된 체크리스트 서브 아이템 아이디").type(
                        JsonFieldType.NUMBER),
                    fieldWithPath("contents").description("등록된 체크리스트 서브 아이템 내용")
                        .type(
                            JsonFieldType.STRING),
                    fieldWithPath("isChecked").description(
                        "등록된 체크리스트 서브 아이템 체크 여부").type(
                        JsonFieldType.BOOLEAN)
                )));
  }

  @Test
  @WithMockOAuth2User
  @DisplayName("체크리스트 서브 아이템 체크 여부 수정")
  void modifyChecklistSubItem() throws Exception {
    String url = "/api/v1/checklist/item/{item-id}/sub-item/{sub-item-id}";

    ChecklistSubItemStateDto checklistSubItemStateDto = ChecklistSubItemStateDto.builder()
        .isChecked(false)
        .build();

    // given
    given(checklistItemService.findChecklistItem(anyLong(), anyLong())).willReturn(checklistItem);
    given(checklistSubItemService.modifyChecklistSubItem(anyLong(),
        anyLong(), any(ChecklistSubItemStateDto.class))).willReturn(checklistSubItem);

    // when
    ResultActions result = mockMvc.perform(put(url, CHECKLIST_ITEM_ID, CHECKLIST_SUB_ITEM_ID)
        .header(HttpHeaders.AUTHORIZATION, ACCESS_TOKEN_PREFIX + "ACCESS_TOKEN")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(checklistSubItemStateDto)));

    // then
    result.andExpect(status().isOk())
        .andDo(
            document("checklist/sub-item/checklist-sub-item-modify",
                pathParameters(
                    parameterWithName("item-id").description("체크리스트 아이템 아이디"),
                    parameterWithName("sub-item-id").description("체크리스트 서브 아이템 아이디")
                ), requestFields(
                    fieldWithPath("isChecked").description("수정할 체크리스트 서브 아이템 체크 여부").type(
                        JsonFieldType.BOOLEAN)
                ),
                responseFields(
                    beneathPath("data").withSubsectionId("data"),
                    fieldWithPath("id").description("수정된 체크리스트 서브 아이템 아이디").type(
                        JsonFieldType.NUMBER),
                    fieldWithPath("contents").description("수정된 체크리스트 서브 아이템 내용").type(
                        JsonFieldType.STRING),
                    fieldWithPath("isChecked").description(
                        "수정된 체크리스트 서브 아이템 체크 여부").type(
                        JsonFieldType.BOOLEAN)
                )
            ));
  }

  @Test
  @WithMockOAuth2User
  @DisplayName("체크리스트 서브 아이템 삭제")
  void withdrawChecklistSubItem() throws Exception {
    String url = "/api/v1/checklist/item/{item-id}/sub-item/{sub-item-id}";

    // given
    given(checklistItemService.findChecklistItem(anyLong(), anyLong())).willReturn(checklistItem);
    given(checklistSubItemService.withdrawChecklistSubItem(anyLong(), anyLong())).willReturn(true);

    // when
    ResultActions result = mockMvc.perform(delete(url, CHECKLIST_ITEM_ID, CHECKLIST_SUB_ITEM_ID)
        .header(HttpHeaders.AUTHORIZATION, ACCESS_TOKEN_PREFIX + "ACCESS_TOKEN"));

    // then
    result.andExpect(status().isOk())
        .andDo(
            document("checklist/sub-item/checklist-sub-item-withdraw",
                pathParameters(
                    parameterWithName("item-id").description("체크리스트 아이템 아이디"),
                    parameterWithName("sub-item-id").description("체크리스트 서브 아이템 아이디")
                )
            ));
  }
}
