package com.dnd.weddingmap.domain.transaction.controller;

import com.dnd.weddingmap.domain.member.Member;
import com.dnd.weddingmap.domain.member.service.MemberService;
import com.dnd.weddingmap.domain.oauth.CustomUserDetails;
import com.dnd.weddingmap.domain.transaction.Transaction;
import com.dnd.weddingmap.domain.transaction.dto.TransactionDto;
import com.dnd.weddingmap.domain.transaction.dto.TransactionListResponseDto;
import com.dnd.weddingmap.domain.transaction.service.TransactionService;
import com.dnd.weddingmap.global.exception.InternalServerErrorException;
import com.dnd.weddingmap.global.exception.NotFoundException;
import com.dnd.weddingmap.global.response.SuccessResponse;
import com.dnd.weddingmap.global.util.MessageUtil;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/transaction")
@RequiredArgsConstructor
public class TransactionController {

  private final MemberService memberService;
  private final TransactionService transactionService;

  @PostMapping
  public ResponseEntity<SuccessResponse> createTransaction(
      @AuthenticationPrincipal CustomUserDetails user,
      @RequestBody @Valid TransactionDto requestDto) {
    Member member = memberService.findMember(user.getId())
        .orElseThrow(
            () -> new NotFoundException(MessageUtil.getMessage("member.notFound.exception")));
    TransactionDto savedTransaction = transactionService.createTransaction(
        requestDto, member);

    if (savedTransaction == null) {
      throw new InternalServerErrorException(
          MessageUtil.getMessage("transaction.register.failure"));
    }
    return ResponseEntity.status(HttpStatus.CREATED).body(
        SuccessResponse.builder().httpStatus(HttpStatus.CREATED)
            .message(MessageUtil.getMessage("transaction.register.success"))
            .data(savedTransaction).build());
  }

  @GetMapping("/{transaction-id}")
  public ResponseEntity<SuccessResponse> getTransaction(
      @AuthenticationPrincipal CustomUserDetails user,
      @PathVariable("transaction-id") Long transactionId) {
    Transaction transaction = transactionService.findTransaction(transactionId, user.getId());

    return ResponseEntity.ok()
        .body(SuccessResponse.builder().data(new TransactionDto(transaction)).build());
  }

  @PutMapping("/{transaction-id}")
  public ResponseEntity<SuccessResponse> modifyTransaction(
      @AuthenticationPrincipal CustomUserDetails user,
      @PathVariable("transaction-id") Long transactionId,
      @RequestBody @Valid TransactionDto requestDto) {
    Transaction transaction = transactionService.findTransaction(transactionId, user.getId());
    TransactionDto result = transactionService.modifyTransaction(transaction.getId(), requestDto);

    return ResponseEntity.ok()
        .body(SuccessResponse.builder().data(result).build());
  }

  @DeleteMapping("/{transaction-id}")
  public ResponseEntity<SuccessResponse> withdrawTransaction(
      @AuthenticationPrincipal CustomUserDetails user,
      @PathVariable("transaction-id") Long transactionId) {
    Transaction transaction = transactionService.findTransaction(transactionId, user.getId());

    boolean result = transactionService.withdrawTransaction(transaction.getId());
    if (!result) {
      throw new NotFoundException(MessageUtil.getMessage("transaction.notFound.exception"));
    }
    return ResponseEntity.ok(
        SuccessResponse.builder().message(MessageUtil.getMessage("transaction.withdraw.success")
        ).build());
  }

  @GetMapping
  public ResponseEntity<SuccessResponse> getTransactionList(
      @AuthenticationPrincipal CustomUserDetails user) {
    Member member = memberService.findMember(user.getId())
        .orElseThrow(
            () -> new NotFoundException(MessageUtil.getMessage("member.notFound.exception")));
    List<TransactionListResponseDto> transactionList = transactionService.findTransactionList(
        member.getId());

    return ResponseEntity.ok()
        .body(SuccessResponse.builder().data(transactionList).build());
  }
}
