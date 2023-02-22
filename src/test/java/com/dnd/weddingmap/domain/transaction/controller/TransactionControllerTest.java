package com.dnd.weddingmap.domain.transaction.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.delete;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
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

import com.dnd.weddingmap.docs.springrestdocs.AbstractRestDocsTests;
import com.dnd.weddingmap.domain.jwt.JwtTokenProvider;
import com.dnd.weddingmap.domain.member.Member;
import com.dnd.weddingmap.domain.member.Role;
import com.dnd.weddingmap.domain.member.service.MemberService;
import com.dnd.weddingmap.domain.oauth.OAuth2Provider;
import com.dnd.weddingmap.domain.transaction.Transaction;
import com.dnd.weddingmap.domain.transaction.TransactionCategory;
import com.dnd.weddingmap.domain.transaction.dto.TransactionDto;
import com.dnd.weddingmap.domain.transaction.dto.TransactionListResponseDto;
import com.dnd.weddingmap.domain.transaction.service.TransactionService;
import com.dnd.weddingmap.global.WithMockOAuth2User;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.ResultActions;

@WebMvcTest(TransactionController.class)
class TransactionControllerTest extends AbstractRestDocsTests {

  static final String ACCESS_TOKEN_PREFIX = "Bearer ";
  static final Long TRANSACTION_ID = 1L;
  @MockBean
  TransactionService transactionService;
  @MockBean
  MemberService memberService;
  @MockBean
  JwtTokenProvider jwtTokenProvider;

  private ObjectMapper objectMapper;

  private final Member member = Member.builder()
      .name("test")
      .email("test@example.com")
      .profileImage("test.png")
      .role(Role.USER)
      .oauth2Provider(OAuth2Provider.GOOGLE)
      .build();

  private final Transaction transaction = Transaction.builder()
      .id(1L)
      .title("test title")
      .agency("test agency")
      .transactionDate(LocalDate.now())
      .payment(-1000000L)
      .balance(2000000L)
      .transactionCategory(TransactionCategory.CARD)
      .accountHolder("좋은 웨딩홀")
      .accountNumber("123-123-1234")
      .memo("test memo")
      .member(member)
      .build();

  private final TransactionDto transactionDto = TransactionDto.builder()
      .title("test title")
      .agency("test agency")
      .transactionDate(LocalDate.now())
      .payment(-1000000L)
      .balance(2000000L)
      .transactionCategory(TransactionCategory.CARD)
      .accountHolder("좋은 웨딩홀")
      .accountNumber("123-123-1234")
      .memo("test memo")
      .build();

  @BeforeEach
  void init() {
    objectMapper = new ObjectMapper();
    objectMapper.registerModule(new JavaTimeModule());
  }

  @Test
  @WithMockOAuth2User
  @DisplayName("예산표 등록")
  void createTransaction() throws Exception {
    String url = "/api/v1/budget/transaction";

    // given
    given(memberService.findMember(anyLong())).willReturn(Optional.ofNullable(member));
    given(transactionService.createTransaction(any(TransactionDto.class),
        any(Member.class))).willReturn(new TransactionDto(transaction));

    // when
    ResultActions result = mockMvc.perform(post(url)
        .header(HttpHeaders.AUTHORIZATION, ACCESS_TOKEN_PREFIX + "ACCESS_TOKEN")
        .with(csrf())
        .accept(MediaType.APPLICATION_JSON)
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(transactionDto)));

    // then
    result.andExpect(status().isCreated()).andDo(
        document("budget/transaction/create-transaction",
            requestFields(
                fieldWithPath("id").ignored(),
                fieldWithPath("title").description(
                    "제목 (* required)").type(
                    JsonFieldType.STRING),
                fieldWithPath("agency").description("계약 업체 (* required)")
                    .type(JsonFieldType.STRING),
                fieldWithPath("transactionDate").description("계약 날짜 (* required)")
                    .type(JsonFieldType.ARRAY),
                fieldWithPath("payment").description("계약금 (* required)").type(JsonFieldType.NUMBER),
                fieldWithPath("balance").description("잔금").type(JsonFieldType.NUMBER),
                fieldWithPath("transactionCategory").description("거래 구분 (* required)")
                    .type(JsonFieldType.STRING),
                fieldWithPath("accountHolder").description("예금주").type(JsonFieldType.STRING),
                fieldWithPath("accountNumber").description("계좌 번호").type(JsonFieldType.STRING),
                fieldWithPath("memo").description("메모").type(JsonFieldType.STRING)
            ), responseFields(
                beneathPath("data").withSubsectionId("data"),
                fieldWithPath("id").description(
                    "등록된 예산표 아이디").type(
                    JsonFieldType.NUMBER),
                fieldWithPath("title").description(
                    "제목").type(
                    JsonFieldType.STRING),
                fieldWithPath("agency").description("계약 업체")
                    .type(JsonFieldType.STRING),
                fieldWithPath("transactionDate").description("계약 날짜 (yyyy-mm-dd)")
                    .type(JsonFieldType.STRING),
                fieldWithPath("payment").description("계약금").type(JsonFieldType.NUMBER),
                fieldWithPath("balance").description("잔금").type(JsonFieldType.NUMBER),
                fieldWithPath("transactionCategory").description("거래 구분")
                    .type(JsonFieldType.STRING),
                fieldWithPath("accountHolder").description("예금주").type(JsonFieldType.STRING),
                fieldWithPath("accountNumber").description("계좌 번호").type(JsonFieldType.STRING),
                fieldWithPath("memo").description("메모").type(JsonFieldType.STRING)
            )
        )
    );
  }

  @Test
  @WithMockOAuth2User
  @DisplayName("예산표 상세 조회")
  void getTransaction() throws Exception {
    String url = "/api/v1/budget/transaction/{transaction-id}";

    // given
    given(transactionService.findTransaction(anyLong())).willReturn(
        Optional.ofNullable(transaction));

    // when
    ResultActions result = mockMvc.perform(get(url, TRANSACTION_ID)
        .header(HttpHeaders.AUTHORIZATION, ACCESS_TOKEN_PREFIX + "ACCESS_TOKEN"));

    // then
    result.andExpect(status().isOk()).andDo(
        document("budget/transaction/transaction-detail",
            pathParameters(
                parameterWithName("transaction-id").description("예산표 아이디")
            ),
            responseFields(
                beneathPath("data").withSubsectionId("data"),
                fieldWithPath("id").description(
                    "예산표 아이디").type(
                    JsonFieldType.NUMBER),
                fieldWithPath("title").description(
                    "제목").type(
                    JsonFieldType.STRING),
                fieldWithPath("agency").description("계약 업체")
                    .type(JsonFieldType.STRING),
                fieldWithPath("transactionDate").description("계약 날짜 (yyyy-mm-dd)")
                    .type(JsonFieldType.STRING),
                fieldWithPath("payment").description("계약금").type(JsonFieldType.NUMBER),
                fieldWithPath("balance").description("잔금").type(JsonFieldType.NUMBER),
                fieldWithPath("transactionCategory").description("거래 구분")
                    .type(JsonFieldType.STRING),
                fieldWithPath("accountHolder").description("예금주").type(JsonFieldType.STRING),
                fieldWithPath("accountNumber").description("계좌 번호").type(JsonFieldType.STRING),
                fieldWithPath("memo").description("메모").type(JsonFieldType.STRING)
            )
        )
    );
  }

  @Test
  @WithMockOAuth2User
  @DisplayName("예산표 수정")
  void modifyTransaction() throws Exception {
    String url = "/api/v1/budget/transaction/{transaction-id}";

    // given
    given(transactionService.modifyTransaction(anyLong(), any(TransactionDto.class))).willReturn(
        new TransactionDto(transaction));

    // when
    ResultActions result = mockMvc.perform(put(url, TRANSACTION_ID)
        .header(HttpHeaders.AUTHORIZATION, ACCESS_TOKEN_PREFIX + "ACCESS_TOKEN")
        .with(csrf())
        .accept(MediaType.APPLICATION_JSON)
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(transactionDto)));

    // then
    result.andExpect(status().isOk())
        .andDo(
            document("budget/transaction/modify-transaction",
                pathParameters(
                    parameterWithName("transaction-id").description("수정할 예산표 아이디")
                ), requestFields(
                    fieldWithPath("id").ignored(),
                    fieldWithPath("title").description(
                        "제목").type(
                        JsonFieldType.STRING),
                    fieldWithPath("agency").description("계약 업체")
                        .type(JsonFieldType.STRING),
                    fieldWithPath("transactionDate").description("계약 날짜")
                        .type(JsonFieldType.ARRAY),
                    fieldWithPath("payment").description("계약금").type(JsonFieldType.NUMBER),
                    fieldWithPath("balance").description("잔금").type(JsonFieldType.NUMBER),
                    fieldWithPath("transactionCategory").description("거래 구분")
                        .type(JsonFieldType.STRING),
                    fieldWithPath("accountHolder").description("예금주").type(JsonFieldType.STRING),
                    fieldWithPath("accountNumber").description("계좌 번호").type(JsonFieldType.STRING),
                    fieldWithPath("memo").description("메모").type(JsonFieldType.STRING)
                ), responseFields(
                    beneathPath("data").withSubsectionId("data"),
                    fieldWithPath("id").description(
                        "수정된 예산표 아이디").type(
                        JsonFieldType.NUMBER),
                    fieldWithPath("title").description(
                        "제목").type(
                        JsonFieldType.STRING),
                    fieldWithPath("agency").description("계약 업체")
                        .type(JsonFieldType.STRING),
                    fieldWithPath("transactionDate").description("계약 날짜 (yyyy-mm-dd)")
                        .type(JsonFieldType.STRING),
                    fieldWithPath("payment").description("계약금").type(JsonFieldType.NUMBER),
                    fieldWithPath("balance").description("잔금").type(JsonFieldType.NUMBER),
                    fieldWithPath("transactionCategory").description("거래 구분")
                        .type(JsonFieldType.STRING),
                    fieldWithPath("accountHolder").description("예금주").type(JsonFieldType.STRING),
                    fieldWithPath("accountNumber").description("계좌 번호").type(JsonFieldType.STRING),
                    fieldWithPath("memo").description("메모").type(JsonFieldType.STRING)
                )
            ));
  }

  @Test
  @WithMockOAuth2User
  @DisplayName("예산표 삭제")
  void withdrawTransaction() throws Exception {
    String url = "/api/v1/budget/transaction/{transaction-id}";

    // given
    given(transactionService.withdrawTransaction(anyLong())).willReturn(true);

    // when
    ResultActions result = mockMvc.perform(delete(url, TRANSACTION_ID)
        .header(HttpHeaders.AUTHORIZATION, ACCESS_TOKEN_PREFIX + "ACCESS_TOKEN")
        .with(csrf()));

    // then
    result.andExpect(status().isOk())
        .andDo(
            document("budget/transaction/withdraw-transaction",
                pathParameters(
                    parameterWithName("transaction-id").description("삭제할 예산표 아이디")
                )
            ));
  }
}
