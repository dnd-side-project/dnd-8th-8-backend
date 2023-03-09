package com.dnd.weddingmap.domain.budget.controller;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.payload.PayloadDocumentation.beneathPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.dnd.weddingmap.docs.springrestdocs.AbstractRestDocsTests;
import com.dnd.weddingmap.domain.budget.service.BudgetService;
import com.dnd.weddingmap.domain.jwt.JwtTokenProvider;
import com.dnd.weddingmap.domain.wedding.dto.BudgetDto;
import com.dnd.weddingmap.global.WithMockOAuth2User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.ResultActions;

@WebMvcTest(BudgetController.class)
class BudgetControllerTest extends AbstractRestDocsTests {

  static final String ACCESS_TOKEN_PREFIX = "Bearer ";

  @MockBean
  JwtTokenProvider jwtTokenProvider;

  @MockBean
  BudgetService budgetService;

  @Test
  @DisplayName("현재 남아있는 결혼 예산 조회")
  @WithMockOAuth2User
  void getCurrentBudget() throws Exception {
    String url = "/api/v1/budget";
    // given
    BudgetDto budgetDto = BudgetDto.builder()
        .budget(20000000L)
        .build();

    given(budgetService.getCurrentBudget(anyLong()))
        .willReturn(budgetDto);

    // when
    ResultActions result = mockMvc.perform(get(url)
        .header(HttpHeaders.AUTHORIZATION, ACCESS_TOKEN_PREFIX + "ACCESS_TOKEN"));

    // then
    result.andExpect(status().isOk())
        .andDo(document("budget/get-current-budget",
            responseFields(
                beneathPath("data").withSubsectionId("data"),
                fieldWithPath("budget").description("현재 남아있는 예산").type(
                    JsonFieldType.NUMBER)
            )
        ));
  }
}
