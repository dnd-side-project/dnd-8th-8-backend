package com.dnd.weddingmap.domain.wedding.controller;

import com.dnd.weddingmap.domain.oauth.CustomUserDetails;
import com.dnd.weddingmap.domain.wedding.dto.BudgetDto;
import com.dnd.weddingmap.domain.wedding.dto.WeddingDayDto;
import com.dnd.weddingmap.domain.wedding.service.WeddingService;
import com.dnd.weddingmap.global.response.SuccessResponse;
import com.dnd.weddingmap.global.util.MessageUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/wedding")
@RequiredArgsConstructor
public class WeddingController {

  private final WeddingService weddingService;

  @PostMapping
  public ResponseEntity<SuccessResponse> registerWedding(
      @AuthenticationPrincipal CustomUserDetails user,
      @RequestBody @Valid WeddingDayDto weddingDayDto) {
    weddingService.registerWedding(user.getId(), weddingDayDto);
    return ResponseEntity.status(HttpStatus.CREATED).body(
        SuccessResponse.builder().message(MessageUtil.getMessage("wedding.register.success"))
            .httpStatus(HttpStatus.CREATED).build());
  }

  @GetMapping("/day")
  public ResponseEntity<SuccessResponse> getWeddingDay(
      @AuthenticationPrincipal CustomUserDetails user) {
    WeddingDayDto weddingDayDto = weddingService.getWeddingDay(user.getId());
    return ResponseEntity.ok()
        .body(SuccessResponse.builder()
            .message(MessageUtil.getMessage("wedding.getWeddingDay.success"))
            .data(weddingDayDto).build());
  }

  @PutMapping("/day")
  public ResponseEntity<SuccessResponse> modifyWeddingDay(
      @AuthenticationPrincipal CustomUserDetails user,
      @RequestBody @Valid WeddingDayDto weddingDayDto) {
    weddingService.modifyWeddingDay(user.getId(), weddingDayDto);
    return ResponseEntity.ok().body(
        SuccessResponse.builder()
            .message(MessageUtil.getMessage("wedding.modifyWeddingDay.success"))
            .build());
  }

  @GetMapping("/budget")
  public ResponseEntity<SuccessResponse> getBudget(
      @AuthenticationPrincipal CustomUserDetails user) {
    BudgetDto budgetDto = weddingService.getBudget(user.getId());
    return ResponseEntity.ok()
        .body(SuccessResponse.builder().message(MessageUtil.getMessage("wedding.getBudget.success"))
            .data(budgetDto).build());
  }

  @PutMapping("/budget")
  public ResponseEntity<SuccessResponse> modifyBudget(
      @AuthenticationPrincipal CustomUserDetails user,
      @RequestBody @Valid BudgetDto budgetDto) {
    weddingService.modifyBudget(user.getId(), budgetDto);
    return ResponseEntity.ok().body(
        SuccessResponse.builder().message(MessageUtil.getMessage("wedding.modifyBudget.success"))
            .build());
  }
}
