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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.dnd.weddingmap.docs.springrestdocs.AbstractRestDocsTests;
import com.dnd.weddingmap.domain.jwt.JwtTokenProvider;
import com.dnd.weddingmap.domain.member.Member;
import com.dnd.weddingmap.domain.member.Role;
import com.dnd.weddingmap.domain.member.service.MemberService;
import com.dnd.weddingmap.domain.oauth.OAuth2Provider;
import com.dnd.weddingmap.domain.transaction.PaymentType;
import com.dnd.weddingmap.domain.transaction.Transaction;
import com.dnd.weddingmap.domain.transaction.dto.TransactionDto;
import com.dnd.weddingmap.domain.transaction.dto.TransactionListResponseDto;
import com.dnd.weddingmap.domain.transaction.service.TransactionService;
import com.dnd.weddingmap.global.WithMockOAuth2User;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
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

  @Autowired
  private ObjectMapper objectMapper;

  private final Member member = Member.builder()
      .id(1L)
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
      .paymentType(PaymentType.CARD)
      .accountHolder("?????? ?????????")
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
      .paymentType(PaymentType.CARD)
      .accountHolder("?????? ?????????")
      .accountNumber("123-123-1234")
      .memo("test memo")
      .build();


  @Test
  @WithMockOAuth2User
  @DisplayName("?????? ?????? ??????")
  void createTransaction() throws Exception {
    String url = "/api/v1/transaction";

    // given
    given(memberService.findMember(anyLong())).willReturn(Optional.ofNullable(member));
    given(transactionService.createTransaction(any(TransactionDto.class),
        any(Member.class))).willReturn(new TransactionDto(transaction));

    // when
    ResultActions result = mockMvc.perform(post(url)
        .header(HttpHeaders.AUTHORIZATION, ACCESS_TOKEN_PREFIX + "ACCESS_TOKEN")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(transactionDto)));

    // then
    result.andExpect(status().isCreated()).andDo(
        document("transaction/create-transaction",
            requestFields(
                fieldWithPath("title").description(
                    "?????? (* required)").type(
                    JsonFieldType.STRING),
                fieldWithPath("agency").description("?????? ?????? (* required)")
                    .type(JsonFieldType.STRING),
                fieldWithPath("transactionDate").description("?????? ?????? (* required)")
                    .type(JsonFieldType.STRING),
                fieldWithPath("payment").description("????????? (* required)").type(JsonFieldType.NUMBER),
                fieldWithPath("balance").description("??????").type(JsonFieldType.NUMBER),
                fieldWithPath("paymentType").description("?????? ?????? (* required)")
                    .type(JsonFieldType.STRING),
                fieldWithPath("accountHolder").description("?????????").type(JsonFieldType.STRING),
                fieldWithPath("accountNumber").description("?????? ??????").type(JsonFieldType.STRING),
                fieldWithPath("memo").description("??????").type(JsonFieldType.STRING)
            ), responseFields(
                beneathPath("data").withSubsectionId("data"),
                fieldWithPath("id").description(
                    "????????? ?????? ?????? ?????????").type(
                    JsonFieldType.NUMBER),
                fieldWithPath("title").description(
                    "??????").type(
                    JsonFieldType.STRING),
                fieldWithPath("agency").description("?????? ??????")
                    .type(JsonFieldType.STRING),
                fieldWithPath("transactionDate").description("?????? ??????")
                    .type(JsonFieldType.STRING),
                fieldWithPath("payment").description("?????????").type(JsonFieldType.NUMBER),
                fieldWithPath("balance").description("??????").type(JsonFieldType.NUMBER),
                fieldWithPath("paymentType").description("?????? ??????")
                    .type(JsonFieldType.STRING),
                fieldWithPath("accountHolder").description("?????????").type(JsonFieldType.STRING),
                fieldWithPath("accountNumber").description("?????? ??????").type(JsonFieldType.STRING),
                fieldWithPath("memo").description("??????").type(JsonFieldType.STRING)
            )
        )
    );
  }

  @Test
  @WithMockOAuth2User
  @DisplayName("?????? ?????? ?????? ??????")
  void getTransaction() throws Exception {
    String url = "/api/v1/transaction/{transaction-id}";

    // given
    given(transactionService.findTransaction(anyLong(), anyLong())).willReturn(transaction);

    // when
    ResultActions result = mockMvc.perform(get(url, TRANSACTION_ID)
        .header(HttpHeaders.AUTHORIZATION, ACCESS_TOKEN_PREFIX + "ACCESS_TOKEN"));

    // then
    result.andExpect(status().isOk()).andDo(
        document("transaction/transaction-detail",
            pathParameters(
                parameterWithName("transaction-id").description("?????? ?????? ?????????")
            ),
            responseFields(
                beneathPath("data").withSubsectionId("data"),
                fieldWithPath("id").description(
                    "?????? ?????? ?????????").type(
                    JsonFieldType.NUMBER),
                fieldWithPath("title").description(
                    "??????").type(
                    JsonFieldType.STRING),
                fieldWithPath("agency").description("?????? ??????")
                    .type(JsonFieldType.STRING),
                fieldWithPath("transactionDate").description("?????? ??????")
                    .type(JsonFieldType.STRING),
                fieldWithPath("payment").description("?????????").type(JsonFieldType.NUMBER),
                fieldWithPath("balance").description("??????").type(JsonFieldType.NUMBER),
                fieldWithPath("paymentType").description("?????? ??????")
                    .type(JsonFieldType.STRING),
                fieldWithPath("accountHolder").description("?????????").type(JsonFieldType.STRING),
                fieldWithPath("accountNumber").description("?????? ??????").type(JsonFieldType.STRING),
                fieldWithPath("memo").description("??????").type(JsonFieldType.STRING)
            )
        )
    );
  }

  @Test
  @WithMockOAuth2User
  @DisplayName("?????? ?????? ??????")
  void modifyTransaction() throws Exception {
    String url = "/api/v1/transaction/{transaction-id}";

    // given
    given(transactionService.findTransaction(anyLong(), anyLong())).willReturn(transaction);
    given(transactionService.modifyTransaction(anyLong(), any(TransactionDto.class))).willReturn(
        new TransactionDto(transaction));

    // when
    ResultActions result = mockMvc.perform(put(url, TRANSACTION_ID)
        .header(HttpHeaders.AUTHORIZATION, ACCESS_TOKEN_PREFIX + "ACCESS_TOKEN")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(transactionDto)));

    // then
    result.andExpect(status().isOk())
        .andDo(
            document("transaction/modify-transaction",
                pathParameters(
                    parameterWithName("transaction-id").description("????????? ?????? ?????? ?????????")
                ), requestFields(
                    fieldWithPath("title").description(
                        "??????").type(
                        JsonFieldType.STRING),
                    fieldWithPath("agency").description("?????? ??????")
                        .type(JsonFieldType.STRING),
                    fieldWithPath("transactionDate").description("?????? ??????")
                        .type(JsonFieldType.STRING),
                    fieldWithPath("payment").description("?????????").type(JsonFieldType.NUMBER),
                    fieldWithPath("balance").description("??????").type(JsonFieldType.NUMBER),
                    fieldWithPath("paymentType").description("?????? ??????")
                        .type(JsonFieldType.STRING),
                    fieldWithPath("accountHolder").description("?????????").type(JsonFieldType.STRING),
                    fieldWithPath("accountNumber").description("?????? ??????").type(JsonFieldType.STRING),
                    fieldWithPath("memo").description("??????").type(JsonFieldType.STRING)
                ), responseFields(
                    beneathPath("data").withSubsectionId("data"),
                    fieldWithPath("id").description(
                        "????????? ?????? ?????? ?????????").type(
                        JsonFieldType.NUMBER),
                    fieldWithPath("title").description(
                        "??????").type(
                        JsonFieldType.STRING),
                    fieldWithPath("agency").description("?????? ??????")
                        .type(JsonFieldType.STRING),
                    fieldWithPath("transactionDate").description("?????? ??????")
                        .type(JsonFieldType.STRING),
                    fieldWithPath("payment").description("?????????").type(JsonFieldType.NUMBER),
                    fieldWithPath("balance").description("??????").type(JsonFieldType.NUMBER),
                    fieldWithPath("paymentType").description("?????? ??????")
                        .type(JsonFieldType.STRING),
                    fieldWithPath("accountHolder").description("?????????").type(JsonFieldType.STRING),
                    fieldWithPath("accountNumber").description("?????? ??????").type(JsonFieldType.STRING),
                    fieldWithPath("memo").description("??????").type(JsonFieldType.STRING)
                )
            ));
  }

  @Test
  @WithMockOAuth2User
  @DisplayName("?????? ?????? ??????")
  void withdrawTransaction() throws Exception {
    String url = "/api/v1/transaction/{transaction-id}";

    // given
    given(transactionService.findTransaction(anyLong(), anyLong())).willReturn(transaction);
    given(transactionService.withdrawTransaction(anyLong())).willReturn(true);

    // when
    ResultActions result = mockMvc.perform(delete(url, TRANSACTION_ID)
        .header(HttpHeaders.AUTHORIZATION, ACCESS_TOKEN_PREFIX + "ACCESS_TOKEN"));

    // then
    result.andExpect(status().isOk())
        .andDo(
            document("transaction/withdraw-transaction",
                pathParameters(
                    parameterWithName("transaction-id").description("????????? ?????? ?????? ?????????")
                )
            ));
  }

  @Test
  @WithMockOAuth2User
  @DisplayName("?????? ?????? ????????? ??????")
  void getTransactionList() throws Exception {
    String url = "/api/v1/transaction";

    TransactionListResponseDto transactionListResponseDto1 = TransactionListResponseDto.builder()
        .id(1L)
        .title("test title1")
        .agency("test agency1")
        .transactionDate(LocalDate.now())
        .payment(-1000000L)
        .paymentType(PaymentType.CARD)
        .build();

    TransactionListResponseDto transactionListResponseDto2 = TransactionListResponseDto.builder()
        .id(2L)
        .title("test title2")
        .agency("test agency2")
        .transactionDate(LocalDate.now())
        .payment(-2000000L)
        .paymentType(PaymentType.CASH)
        .build();

    // given
    given(memberService.findMember(anyLong())).willReturn(Optional.ofNullable(member));
    given(transactionService.findTransactionList(anyLong())).willReturn(
        List.of(transactionListResponseDto1,
            transactionListResponseDto2));

    // when
    ResultActions result = mockMvc.perform(get(url)
        .header(HttpHeaders.AUTHORIZATION, ACCESS_TOKEN_PREFIX + "ACCESS_TOKEN"));

    // then
    result.andExpect(status().isOk())
        .andDo(
            document("transaction/get-transaction-list",
                responseFields(
                    beneathPath("data").withSubsectionId("data"),
                    fieldWithPath("id").description("?????? ?????? ?????????").type(
                        JsonFieldType.NUMBER),
                    fieldWithPath("title").description("??????").type(
                        JsonFieldType.STRING),
                    fieldWithPath("agency").description("?????? ??????")
                        .type(JsonFieldType.STRING),
                    fieldWithPath("transactionDate").description("?????? ??????")
                        .type(JsonFieldType.STRING),
                    fieldWithPath("payment").description("?????????").type(JsonFieldType.NUMBER),
                    fieldWithPath("paymentType").description("?????? ?????? (CARD / CASH)")
                        .type(JsonFieldType.STRING)
                )
            ));
  }
}
