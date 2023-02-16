package com.dnd.wedding.domain.checklist.checklistitem.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.delete;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.put;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.beneathPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.dnd.wedding.domain.checklist.checklistitem.ChecklistItem;
import com.dnd.wedding.domain.checklist.checklistitem.dto.ChecklistItemApiDto;
import com.dnd.wedding.domain.checklist.checklistitem.dto.ChecklistItemDto;
import com.dnd.wedding.domain.checklist.checklistitem.service.ChecklistItemService;
import com.dnd.wedding.domain.checklist.checklistsubitem.dto.ChecklistSubItemDto;
import com.dnd.wedding.domain.checklist.checklistsubitem.service.ChecklistSubItemService;
import com.dnd.wedding.domain.jwt.JwtTokenProvider;
import com.dnd.wedding.domain.member.Member;
import com.dnd.wedding.domain.member.MemberRepository;
import com.dnd.wedding.domain.member.Role;
import com.dnd.wedding.domain.oauth.OAuth2Provider;
import com.dnd.wedding.global.WithMockOAuth2User;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.MockMvc;

@AutoConfigureRestDocs
@ExtendWith(RestDocumentationExtension.class)
@WebMvcTest(ChecklistItemController.class)
class ChecklistItemControllerTest {

  static final Long CHECKLIST_ITEM_ID = 1L;
  @Autowired
  MockMvc mockMvc;

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
      .time(LocalTime.now())
      .place("place")
      .memo("memo")
      .build();

  private final ChecklistItemDto checklistItemDto = ChecklistItemDto.builder()
      .id(1L)
      .title("title")
      .checkDate(LocalDate.now())
      .time(LocalTime.now())
      .place("place")
      .memo("memo")
      .build();

  private final ChecklistSubItemDto checklistSubItemDto1 = ChecklistSubItemDto.builder()
      .id(1L)
      .contents("contents 1")
      .isChecked(false)
      .build();
  private final ChecklistSubItemDto checklistSubItemDto2 = ChecklistSubItemDto.builder()
      .id(2L)
      .contents("contents 2")
      .isChecked(false)
      .build();

  private final ChecklistItemApiDto checklistItemApiDto = ChecklistItemApiDto.builder()
      .checklistItem(checklistItemDto)
      .checklistSubItems(List.of(checklistSubItemDto1, checklistSubItemDto2))
      .build();

  @BeforeEach
  void init() {
    objectMapper = new ObjectMapper();
    objectMapper.registerModule(new JavaTimeModule());
  }

  @Test
  @WithMockOAuth2User
  @DisplayName("아이디를 통한 체크리스트 아이템 조회")
  void getChecklistItemDetail() throws Exception {
    String url = "/api/v1/checklist/item/{item-id}";

    when(checklistItemService.findChecklistItemById(anyLong())).thenReturn(
        Optional.ofNullable(checklistItem));

    when(checklistSubItemService.findChecklistSubItems(anyLong())).thenReturn(
        List.of(checklistSubItemDto1, checklistSubItemDto2));

    mockMvc.perform(get(url, CHECKLIST_ITEM_ID)).andExpect(status().isOk())
        .andDo(print())
        .andDo(
            document("checklist/item/checklist-item-detail",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                pathParameters(
                    parameterWithName("item-id").description("체크리스트 아이템 아이디")
                ),
                responseFields(
                    beneathPath("data").withSubsectionId("data"),
                    fieldWithPath("checklistItem.id").description("체크리스트 아이템 아이디").type(
                        JsonFieldType.NUMBER),
                    fieldWithPath("checklistItem.title").description("체크리스트 아이템 제목").type(
                        JsonFieldType.STRING),
                    fieldWithPath("checklistItem.checkDate").description(
                        "체크리스트 아이템 일정 날짜 (yyyy-mm-dd)").type(
                        JsonFieldType.STRING),
                    fieldWithPath("checklistItem.time").description(
                        "체크리스트 아이템 일정 시간 (hh:mm:ss)").type(
                        JsonFieldType.STRING),
                    fieldWithPath("checklistItem.place").description("체크리스트 아이템 일정 장소").type(
                        JsonFieldType.STRING),
                    fieldWithPath("checklistItem.memo").description("체크리스트 아이템 메모").type(
                        JsonFieldType.STRING),
                    fieldWithPath("checklistSubItems[].id").description("체크리스트 서브 아이템 아이디")
                        .type(
                            JsonFieldType.NUMBER),
                    fieldWithPath("checklistSubItems[].contents").description("체크리스트 서브 아이템 내용")
                        .type(
                            JsonFieldType.STRING),
                    fieldWithPath("checklistSubItems[].isChecked").description(
                        "체크리스트 서브 아이템 체크 여부").type(
                        JsonFieldType.BOOLEAN)
                )
            ));
  }

  @Test
  @WithMockOAuth2User
  @DisplayName("체크리스트 아이템 생성")
  void createChecklistItem() throws Exception {
    String url = "/api/v1/checklist/item";

    Member member = Member.builder()
        .name("test")
        .email("test@example.com")
        .profileImage("test.png")
        .role(Role.USER)
        .oauth2Provider(OAuth2Provider.GOOGLE)
        .build();

    when(memberRepository.findById(anyLong())).thenReturn(Optional.ofNullable(member));
    when(checklistItemService.createChecklistItem(any(ChecklistItemApiDto.class),
        any(Member.class))).thenReturn(checklistItemApiDto);

    mockMvc.perform(post(url)
            .with(csrf())
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(checklistItemApiDto)))
        .andDo(print())
        .andExpect(status().isCreated())
        .andDo(
            document("checklist/item/checklist-item-create",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                requestFields(
                    fieldWithPath("checklistItem.id").ignored(),
                    fieldWithPath("checklistItem.title").description("등록할 체크리스트 아이템 제목").type(
                        JsonFieldType.STRING),
                    fieldWithPath("checklistItem.checkDate").description(
                        "등록할 체크리스트 아이템 일정 날짜 ex: [yyyy,mm,dd] ").type(
                        JsonFieldType.ARRAY),
                    fieldWithPath("checklistItem.time").description(
                        "등록할 체크리스트 아이템 일정 시간 ex: [hh,mm,ss] ").type(
                        JsonFieldType.ARRAY).optional(),
                    fieldWithPath("checklistItem.place").description("등록할 체크리스트 아이템 일정 장소").type(
                        JsonFieldType.STRING).optional(),
                    fieldWithPath("checklistItem.memo").description("등록할 체크리스트 아이템 메모").type(
                        JsonFieldType.STRING).optional(),
                    fieldWithPath("checklistSubItems[].id").ignored(),
                    fieldWithPath("checklistSubItems[].contents").description("등록할 체크리스트 서브 아이템 내용")
                        .type(
                            JsonFieldType.STRING),
                    fieldWithPath("checklistSubItems[].isChecked").ignored()
                ),
                responseFields(
                    beneathPath("data").withSubsectionId("data"),
                    fieldWithPath("checklistItem.id").description("등록된 체크리스트 아이템 아이디").type(
                        JsonFieldType.NUMBER),
                    fieldWithPath("checklistItem.title").description("등록된 체크리스트 아이템 제목").type(
                        JsonFieldType.STRING),
                    fieldWithPath("checklistItem.checkDate").description(
                        "등록된 체크리스트 아이템 일정 날짜 (yyyy-mm-dd)").type(
                        JsonFieldType.STRING),
                    fieldWithPath("checklistItem.time").description(
                        "등록된 체크리스트 아이템 일정 시간 (hh:mm:ss)").type(
                        JsonFieldType.STRING),
                    fieldWithPath("checklistItem.place").description("등록된 체크리스트 아이템 일정 장소").type(
                        JsonFieldType.STRING),
                    fieldWithPath("checklistItem.memo").description("등록된 체크리스트 아이템 메모").type(
                        JsonFieldType.STRING),
                    fieldWithPath("checklistSubItems[].id").description("등록된 체크리스트 서브 아이템 아이디")
                        .type(
                            JsonFieldType.NUMBER),
                    fieldWithPath("checklistSubItems[].contents").description("등록된 체크리스트 서브 아이템 내용")
                        .type(
                            JsonFieldType.STRING),
                    fieldWithPath("checklistSubItems[].isChecked").description(
                        "등록된 체크리스트 서브 아이템 체크 여부").type(
                        JsonFieldType.BOOLEAN)
                )
            ));
  }

  @Test
  @WithMockOAuth2User
  @DisplayName("체크리스트 아이템 수정")
  void modifyChecklistItem() throws Exception {
    String url = "/api/v1/checklist/item/{item-id}";

    when(checklistItemService.findChecklistItemById(anyLong())).thenReturn(
        Optional.ofNullable(checklistItem));

    when(checklistItemService.modifyChecklistItem(anyLong(),
        any(ChecklistItemApiDto.class))).thenReturn(checklistItemApiDto);

    mockMvc.perform(put(url, CHECKLIST_ITEM_ID)
            .with(csrf())
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(checklistItemApiDto)))
        .andExpect(status().isOk())
        .andDo(print())
        .andDo(
            document("checklist/item/checklist-item-modify",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                pathParameters(
                    parameterWithName("item-id").description("체크리스트 아이템 아이디")
                ), requestFields(
                    fieldWithPath("checklistItem.id").ignored(),
                    fieldWithPath("checklistItem.title").description("수정할 체크리스트 아이템 제목").type(
                        JsonFieldType.STRING),
                    fieldWithPath("checklistItem.checkDate").description(
                        "수정할 체크리스트 아이템 일정 날짜 ex: [yyyy,mm,dd] ").type(
                        JsonFieldType.ARRAY),
                    fieldWithPath("checklistItem.time").description(
                        "수정할 체크리스트 아이템 일정 시간 ex: [hh,mm,ss] ").type(
                        JsonFieldType.ARRAY).optional(),
                    fieldWithPath("checklistItem.place").description("수정할 체크리스트 아이템 일정 장소").type(
                        JsonFieldType.STRING).optional(),
                    fieldWithPath("checklistItem.memo").description("수정할 체크리스트 아이템 메모").type(
                        JsonFieldType.STRING).optional(),
                    fieldWithPath("checklistSubItems[].id").description("수정할 체크리스트 서브 아이템 아이디"),
                    fieldWithPath("checklistSubItems[].contents").description("수정할 체크리스트 서브 아이템 내용")
                        .type(
                            JsonFieldType.STRING),
                    fieldWithPath("checklistSubItems[].isChecked").description(
                        "수정할 체크리스트 서브 아이템 체크 여부")
                ),
                responseFields(
                    beneathPath("data").withSubsectionId("data"),
                    fieldWithPath("checklistItem.id").description("수정된 체크리스트 아이템 아이디").type(
                        JsonFieldType.NUMBER),
                    fieldWithPath("checklistItem.title").description("수정된 체크리스트 아이템 제목").type(
                        JsonFieldType.STRING),
                    fieldWithPath("checklistItem.checkDate").description(
                        "수정된 체크리스트 아이템 일정 날짜 (yyyy-mm-dd)").type(
                        JsonFieldType.STRING),
                    fieldWithPath("checklistItem.time").description(
                        "수정된 체크리스트 아이템 일정 시간 (hh:mm:ss)").type(
                        JsonFieldType.STRING),
                    fieldWithPath("checklistItem.place").description("수정된 체크리스트 아이템 일정 장소").type(
                        JsonFieldType.STRING),
                    fieldWithPath("checklistItem.memo").description("수정된 체크리스트 아이템 메모").type(
                        JsonFieldType.STRING),
                    fieldWithPath("checklistSubItems[].id").description("수정된 체크리스트 서브 아이템 아이디")
                        .type(
                            JsonFieldType.NUMBER),
                    fieldWithPath("checklistSubItems[].contents").description("수정된 체크리스트 서브 아이템 내용")
                        .type(
                            JsonFieldType.STRING),
                    fieldWithPath("checklistSubItems[].isChecked").description(
                        "수정된 체크리스트 서브 아이템 체크 여부").type(
                        JsonFieldType.BOOLEAN)
                )
            ));
  }

  @Test
  @WithMockOAuth2User
  @DisplayName("체크리스트 아이템 삭제")
  void withdrawChecklistItem() throws Exception {
    String url = "/api/v1/checklist/item/{item-id}";

    when(checklistItemService.withdrawChecklistItem(anyLong())).thenReturn(true);
    mockMvc.perform(delete(url, CHECKLIST_ITEM_ID).with(csrf())).andExpect(status().isOk())
        .andDo(print())
        .andDo(
            document("checklist/item/checklist-item-withdraw",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                pathParameters(
                    parameterWithName("item-id").description("체크리스트 아이템 아이디")
                )
            ));
  }
}
