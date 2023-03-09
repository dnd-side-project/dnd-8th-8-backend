package com.dnd.weddingmap.domain.budget.controller;

import com.dnd.weddingmap.domain.budget.service.BudgetService;
import com.dnd.weddingmap.domain.oauth.CustomUserDetails;
import com.dnd.weddingmap.domain.wedding.dto.BudgetDto;
import com.dnd.weddingmap.global.response.SuccessResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/budget")
@RequiredArgsConstructor
public class BudgetController {

  private final BudgetService budgetService;

  @GetMapping
  public ResponseEntity<SuccessResponse> getCurrentBudget(
      @AuthenticationPrincipal CustomUserDetails user) {
    BudgetDto currentBudget = budgetService.getCurrentBudget(user.getId());

    return ResponseEntity.ok().body(SuccessResponse.builder().data(currentBudget).build());
  }
}
