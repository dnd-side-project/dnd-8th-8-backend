package com.dnd.weddingmap.domain.transaction.controller;

import com.dnd.weddingmap.domain.member.Member;
import com.dnd.weddingmap.domain.member.service.MemberService;
import com.dnd.weddingmap.domain.oauth.CustomUserDetails;
import com.dnd.weddingmap.domain.transaction.dto.TransactionDto;
import com.dnd.weddingmap.domain.transaction.service.TransactionService;
import com.dnd.weddingmap.global.exception.InternalServerErrorException;
import com.dnd.weddingmap.global.exception.NotFoundException;
import com.dnd.weddingmap.global.response.SuccessResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/budget/transaction")
public class TransactionController {

  private final MemberService memberService;
  private final TransactionService transactionService;

  @PostMapping
  public ResponseEntity<SuccessResponse> createTransaction(
      @AuthenticationPrincipal CustomUserDetails user,
      @RequestBody @Valid TransactionDto requestDto) {
    Member member = memberService.findMember(user.getId())
        .orElseThrow(() -> new NotFoundException("존재하지 않는 사용자입니다."));
    TransactionDto savedTransaction = transactionService.createTransaction(
        requestDto, member);

    if (savedTransaction == null) {
      throw new InternalServerErrorException("예산표 등록에 실패하였습니다.");
    }
    return ResponseEntity.status(HttpStatus.CREATED).body(
        SuccessResponse.builder().httpStatus(HttpStatus.CREATED).message("예산표 등록 성공")
            .data(savedTransaction).build());
  }
}
