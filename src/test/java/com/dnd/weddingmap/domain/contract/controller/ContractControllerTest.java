package com.dnd.weddingmap.domain.contract.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.delete;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.multipart;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.put;
import static org.springframework.restdocs.payload.PayloadDocumentation.beneathPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestPartFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.partWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.restdocs.request.RequestDocumentation.requestParts;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.dnd.weddingmap.docs.springrestdocs.AbstractRestDocsTests;
import com.dnd.weddingmap.domain.contract.Contract;
import com.dnd.weddingmap.domain.contract.ContractStatus;
import com.dnd.weddingmap.domain.contract.dto.ContractDto;
import com.dnd.weddingmap.domain.contract.dto.ContractListResponseDto;
import com.dnd.weddingmap.domain.contract.service.ContractService;
import com.dnd.weddingmap.domain.jwt.JwtTokenProvider;
import com.dnd.weddingmap.domain.member.Member;
import com.dnd.weddingmap.domain.member.Role;
import com.dnd.weddingmap.domain.member.service.MemberService;
import com.dnd.weddingmap.domain.oauth.OAuth2Provider;
import com.dnd.weddingmap.global.WithMockOAuth2User;
import com.dnd.weddingmap.global.service.S3Service;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.nio.charset.StandardCharsets;
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
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.web.multipart.MultipartFile;

@WebMvcTest(ContractController.class)
class ContractControllerTest extends AbstractRestDocsTests {

  static final Long CONTRACT_ID = 1L;
  static final String ACCESS_TOKEN_PREFIX = "Bearer ";

  final Member member = Member.builder()
      .id(1L)
      .name("test")
      .email("test@example.com")
      .profileImage("test.png")
      .role(Role.USER)
      .oauth2Provider(OAuth2Provider.GOOGLE)
      .build();

  final Contract contract = Contract.builder()
      .id(1L)
      .title("test_title")
      .contents("test_contents")
      .contractDate(LocalDate.now())
      .contractStatus(ContractStatus.VERBAL)
      .file("https://storage.com/contract/1")
      .memo("test_memo")
      .member(member)
      .build();

  final ContractDto contractDto = ContractDto.builder()
      .title("test_title")
      .contents("test_contents")
      .contractDate(LocalDate.now())
      .contractStatus(ContractStatus.VERBAL)
      .memo("test_memo")
      .build();

  @MockBean
  ContractService contractService;
  @MockBean
  S3Service s3Service;
  @MockBean
  MemberService memberService;
  @MockBean
  JwtTokenProvider jwtTokenProvider;

  ObjectMapper objectMapper;

  @BeforeEach
  void init() {
    objectMapper = new ObjectMapper();
    objectMapper.registerModule(new JavaTimeModule());
  }

  @Test
  @WithMockOAuth2User
  @DisplayName("계약서 등록")
  void createContract() throws Exception {
    String url = "/api/v1/contract";

    // given
    MockMultipartFile contractFile = new MockMultipartFile(
        "file", "contract.png", "contract/png", "contract data".getBytes());
    given(s3Service.upload(any(MultipartFile.class), eq("contract")))
        .willReturn("https://storage.com/contract/1");

    given(memberService.findMember(anyLong())).willReturn(Optional.ofNullable(member));
    given(contractService.createContract(any(ContractDto.class), any(Member.class))).willReturn(
        new ContractDto(contract));

    String contractJsonDto = objectMapper.writeValueAsString(contractDto);
    MockMultipartFile data = new MockMultipartFile("data", "contract", "application/json",
        contractJsonDto.getBytes(
            StandardCharsets.UTF_8));

    // when
    ResultActions result = mockMvc.perform(multipart(url)
        .file(contractFile)
        .file(data)
        .header(HttpHeaders.AUTHORIZATION, ACCESS_TOKEN_PREFIX + "ACCESS_TOKEN")
        .contentType(MediaType.MULTIPART_FORM_DATA));

    // then
    result.andExpect(status().isCreated())
        .andDo(document("contract/create-contract",
            requestParts(
                partWithName("file").description("계약서 파일"),
                partWithName("data").description("계약서 전체 내용")
            ),
            requestPartFields("data",
                fieldWithPath("id").ignored(),
                fieldWithPath("title").description("계약서 제목 (* required)")
                    .type(JsonFieldType.STRING),
                fieldWithPath("contents").description("계약 내용 (* required)")
                    .type(JsonFieldType.STRING),
                fieldWithPath("contractDate").description("계약 날짜 (* required)")
                    .type(JsonFieldType.ARRAY),
                fieldWithPath("contractStatus").description("계약 상태 (* required)")
                    .type(JsonFieldType.STRING),
                fieldWithPath("memo").description("메모").type(JsonFieldType.STRING),
                fieldWithPath("file").ignored()),
            responseFields(
                beneathPath("data").withSubsectionId("data"),
                fieldWithPath("id").description("등록된 계약서 아이디").type(
                    JsonFieldType.NUMBER),
                fieldWithPath("title").description("계약서 제목").type(
                    JsonFieldType.STRING),
                fieldWithPath("contents").description("계약 내용").type(
                    JsonFieldType.STRING),
                fieldWithPath("contractDate").description(
                    "계약 날짜 (yyyy-mm-dd)").type(
                    JsonFieldType.STRING),
                fieldWithPath("contractStatus").description("계약 상태").type(
                    JsonFieldType.STRING),
                fieldWithPath("file").description("계약서 파일 url").type(
                    JsonFieldType.STRING),
                fieldWithPath("memo").description("메모").type(
                    JsonFieldType.STRING))
        ));
  }

  @Test
  @WithMockOAuth2User
  @DisplayName("계약서 상세 조회")
  void getContractDetail() throws Exception {
    String url = "/api/v1/contract/{contract-id}";

    // given
    given(contractService.findContractById(anyLong())).willReturn(
        Optional.ofNullable(contract));

    // when
    ResultActions result = mockMvc.perform(get(url, CONTRACT_ID)
        .header(HttpHeaders.AUTHORIZATION, ACCESS_TOKEN_PREFIX + "ACCESS_TOKEN"));

    // then
    result.andExpect(status().isOk())
        .andDo(
            document("contract/contract-detail",
                pathParameters(
                    parameterWithName("contract-id").description("계약서 아이디")
                ),
                responseFields(
                    beneathPath("data").withSubsectionId("data"),
                    fieldWithPath("id").description("계약서 아이디").type(
                        JsonFieldType.NUMBER),
                    fieldWithPath("title").description("계약서 제목").type(
                        JsonFieldType.STRING),
                    fieldWithPath("contents").description("계약 내용").type(
                        JsonFieldType.STRING),
                    fieldWithPath("contractDate").description(
                        "계약 날짜 (yyyy-mm-dd)").type(
                        JsonFieldType.STRING),
                    fieldWithPath("contractStatus").description("계약 상태").type(
                        JsonFieldType.STRING),
                    fieldWithPath("file").description("계약서 파일 url").type(
                        JsonFieldType.STRING),
                    fieldWithPath("memo").description("메모").type(
                        JsonFieldType.STRING))
            ));
  }

  @Test
  @WithMockOAuth2User
  @DisplayName("계약서 삭제")
  void withdrawContract() throws Exception {
    String url = "/api/v1/contract/{contract-id}";

    // given
    given(contractService.findContractById(anyLong())).willReturn(Optional.ofNullable(contract));
    doNothing().when(s3Service).delete(anyString(), eq("contract"));
    given(contractService.withdrawContract(anyLong())).willReturn(true);

    // when
    ResultActions result = mockMvc.perform(delete(url, CONTRACT_ID)
        .header(HttpHeaders.AUTHORIZATION, ACCESS_TOKEN_PREFIX + "ACCESS_TOKEN"));

    // then
    result.andExpect(status().isOk())
        .andDo(
            document("contract/contract-withdraw",
                pathParameters(
                    parameterWithName("contract-id").description("계약서 아이디")
                )
            ));
  }

  @Test
  @WithMockOAuth2User
  @DisplayName("계약서 파일 수정")
  void modifyContractFile() throws Exception {
    String url = "/api/v1/contract/{contract-id}";

    // given
    MockMultipartFile contractFile = new MockMultipartFile(
        "file", "contract.png", "contract/png", "contract data".getBytes());

    given(contractService.findContractById(anyLong())).willReturn(Optional.ofNullable(contract));
    doNothing().when(s3Service).delete(anyString(), eq("contract"));
    given(s3Service.upload(any(MultipartFile.class), eq("contract")))
        .willReturn("https://storage.com/contract/1");
    given(contractService.modifyContractFile(anyLong(), anyString())).willReturn(
        new ContractDto(contract));

    // when
    ResultActions result = mockMvc.perform(multipart(url, CONTRACT_ID)
        .file(contractFile)
        .header(HttpHeaders.AUTHORIZATION, ACCESS_TOKEN_PREFIX + "ACCESS_TOKEN")
        .contentType(MediaType.MULTIPART_FORM_DATA));

    // then
    result.andExpect(status().isOk())
        .andDo(document("contract/modify-contract-file",
            pathParameters(
                parameterWithName("contract-id").description("계약서 아이디")
            ),
            requestParts(
                partWithName("file").description("수정할 계약서 파일")
            ),
            responseFields(
                beneathPath("data").withSubsectionId("data"),
                fieldWithPath("id").description("계약서 아이디").type(
                    JsonFieldType.NUMBER),
                fieldWithPath("title").description("계약서 제목").type(
                    JsonFieldType.STRING),
                fieldWithPath("contents").description("계약 내용").type(
                    JsonFieldType.STRING),
                fieldWithPath("contractDate").description(
                    "계약 날짜 (yyyy-mm-dd)").type(
                    JsonFieldType.STRING),
                fieldWithPath("contractStatus").description("계약 상태").type(
                    JsonFieldType.STRING),
                fieldWithPath("file").description("계약서 파일 url").type(
                    JsonFieldType.STRING),
                fieldWithPath("memo").description("메모").type(
                    JsonFieldType.STRING))
        ));
  }

  @Test
  @WithMockOAuth2User
  @DisplayName("계약서 내용 수정")
  void modifyContract() throws Exception {
    String url = "/api/v1/contract/{contract-id}";

    // given
    given(contractService.findContractById(anyLong())).willReturn(Optional.ofNullable(contract));
    given(contractService.modifyContract(anyLong(), any(ContractDto.class))).willReturn(
        new ContractDto(contract));

    // when
    ResultActions result = mockMvc.perform(put(url, CONTRACT_ID)
        .header(HttpHeaders.AUTHORIZATION, ACCESS_TOKEN_PREFIX + "ACCESS_TOKEN")
        .accept(MediaType.APPLICATION_JSON)
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(contractDto)));

    // then
    result.andExpect(status().isOk())
        .andDo(
            document("contract/modify-contract",
                pathParameters(
                    parameterWithName("contract-id").description("계약서 아이디")
                ), requestFields(
                    fieldWithPath("id").ignored(),
                    fieldWithPath("title").description("계약서 제목")
                        .type(JsonFieldType.STRING),
                    fieldWithPath("contents").description("계약 내용")
                        .type(JsonFieldType.STRING),
                    fieldWithPath("contractDate").description("계약 날짜")
                        .type(JsonFieldType.ARRAY),
                    fieldWithPath("contractStatus").description("계약 상태")
                        .type(JsonFieldType.STRING),
                    fieldWithPath("memo").description("메모").type(JsonFieldType.STRING),
                    fieldWithPath("file").ignored()),
                responseFields(
                    beneathPath("data").withSubsectionId("data"),
                    fieldWithPath("id").description("수정된 계약서 아이디").type(
                        JsonFieldType.NUMBER),
                    fieldWithPath("title").description("계약서 제목").type(
                        JsonFieldType.STRING),
                    fieldWithPath("contents").description("계약 내용").type(
                        JsonFieldType.STRING),
                    fieldWithPath("contractDate").description(
                        "계약 날짜 (yyyy-mm-dd)").type(
                        JsonFieldType.STRING),
                    fieldWithPath("contractStatus").description("계약 상태").type(
                        JsonFieldType.STRING),
                    fieldWithPath("file").description("계약서 파일 url").type(
                        JsonFieldType.STRING),
                    fieldWithPath("memo").description("메모").type(
                        JsonFieldType.STRING))
            ));
  }

  @Test
  @WithMockOAuth2User
  @DisplayName("계약서 리스트 조회")
  void getContractList() throws Exception {
    String url = "/api/v1/contract";

    ContractListResponseDto contractListResponseDto1 = ContractListResponseDto.builder()
        .id(1L)
        .title("test_title")
        .contractDate(LocalDate.now())
        .contractStatus(ContractStatus.IN_PROGRESS)
        .build();

    ContractListResponseDto contractListResponseDto2 = ContractListResponseDto.builder()
        .id(2L)
        .title("test_title2")
        .contractDate(LocalDate.now())
        .contractStatus(ContractStatus.COMPLETE)
        .build();

    // given
    given(memberService.findMember(anyLong())).willReturn(Optional.ofNullable(member));
    given(contractService.findContractList(anyLong())).willReturn(
        List.of(contractListResponseDto1, contractListResponseDto2));

    // when
    ResultActions result = mockMvc.perform(get(url)
        .header(HttpHeaders.AUTHORIZATION, ACCESS_TOKEN_PREFIX + "ACCESS_TOKEN"));

    // then
    result.andExpect(status().isOk())
        .andDo(
            document("contract/get-contract-list",
                responseFields(
                    beneathPath("data").withSubsectionId("data"),
                    fieldWithPath("id").description("계약서 아이디").type(
                        JsonFieldType.NUMBER),
                    fieldWithPath("title").description("계약서 제목").type(
                        JsonFieldType.STRING),
                    fieldWithPath("contractDate").description(
                        "계약 날짜 (yyyy-mm-dd)").type(
                        JsonFieldType.STRING),
                    fieldWithPath("contractStatus").description("계약 상태").type(
                        JsonFieldType.STRING)
                )));
  }
}
