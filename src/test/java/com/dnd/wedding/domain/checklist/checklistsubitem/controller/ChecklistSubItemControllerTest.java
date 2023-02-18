package com.dnd.wedding.domain.checklist.checklistsubitem.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
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
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.dnd.wedding.docs.springrestdocs.AbstractRestDocsTests;
import com.dnd.wedding.domain.checklist.checklistitem.ChecklistItem;
import com.dnd.wedding.domain.checklist.checklistitem.service.ChecklistItemService;
import com.dnd.wedding.domain.checklist.checklistsubitem.ChecklistSubItem;
import com.dnd.wedding.domain.checklist.checklistsubitem.dto.ChecklistSubItemDto;
import com.dnd.wedding.domain.checklist.checklistsubitem.dto.UpdateChecklistSubItemDto;
import com.dnd.wedding.domain.checklist.checklistsubitem.service.ChecklistSubItemService;
import com.dnd.wedding.domain.jwt.JwtTokenProvider;
import com.dnd.wedding.domain.member.MemberRepository;
import com.dnd.wedding.global.WithMockOAuth2User;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.ResultActions;

@WebMvcTest(ChecklistSubItemController.class)
class ChecklistSubItemControllerTest extends AbstractRestDocsTests {

  static final Long CHECKLIST_ITEM_ID = 1L;
  static final Long CHECKLIST_SUB_ITEM_ID = 1L;

  @MockBean
  ChecklistItemService checklistItemService;
  @MockBean
  ChecklistSubItemService checklistSubItemService;
  @MockBean
  MemberRepository memberRepository;
  @MockBean
  JwtTokenProvider jwtTokenProvider;

  private ObjectMapper objectMapper;

  private final ChecklistItem checklistItem = ChecklistItem.builder()
      .id(1L)
      .title("title")
      .checkDate(LocalDate.now())
      .startTime(LocalTime.MIN)
      .endTime(LocalTime.MAX)
      .place("place")
      .memo("memo")
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
    when(checklistItemService.findChecklistItemById(anyLong())).thenReturn(
        Optional.ofNullable(checklistItem));
    when(checklistSubItemService.saveChecklistSubItem(any(ChecklistSubItemDto.class),
        any(ChecklistItem.class))).thenReturn(checklistSubItem);

    // when
    ResultActions result = mockMvc.perform(post(url, CHECKLIST_ITEM_ID)
        .with(csrf())
        .accept(MediaType.APPLICATION_JSON)
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

    UpdateChecklistSubItemDto updateChecklistSubItemDto = UpdateChecklistSubItemDto.builder()
        .isChecked(false)
        .build();

    // given
    when(checklistItemService.findChecklistItemById(anyLong())).thenReturn(
        Optional.ofNullable(checklistItem));
    when(checklistSubItemService.modifyChecklistSubItem(anyLong(),
        anyLong(), any(UpdateChecklistSubItemDto.class))).thenReturn(checklistSubItem);

    // when
    ResultActions result = mockMvc.perform(put(url, CHECKLIST_ITEM_ID, CHECKLIST_SUB_ITEM_ID)
        .with(csrf())
        .accept(MediaType.APPLICATION_JSON)
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(updateChecklistSubItemDto)));

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
    when(checklistItemService.findChecklistItemById(anyLong())).thenReturn(
        Optional.ofNullable(checklistItem));
    when(checklistSubItemService.withdrawChecklistSubItem(anyLong(), anyLong())).thenReturn(true);

    // when
    ResultActions result = mockMvc.perform(delete(url, CHECKLIST_ITEM_ID, CHECKLIST_SUB_ITEM_ID)
        .with(csrf()));

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
