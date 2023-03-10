package com.dnd.weddingmap.domain.budget.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.dnd.weddingmap.domain.budget.service.impl.BudgetServiceImpl;
import com.dnd.weddingmap.domain.member.Member;
import com.dnd.weddingmap.domain.member.MemberRepository;
import com.dnd.weddingmap.domain.transaction.Transaction;
import com.dnd.weddingmap.domain.transaction.repository.TransactionRepository;
import com.dnd.weddingmap.domain.wedding.Wedding;
import com.dnd.weddingmap.domain.wedding.dto.BudgetDto;
import com.dnd.weddingmap.global.exception.NotFoundException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class BudgetServiceTest {

  @InjectMocks
  BudgetServiceImpl budgetService;
  @Mock
  MemberRepository memberRepository;
  @Mock
  TransactionRepository transactionRepository;

  Long memberId = 1L;
  Long budget = 1000000L;
  Member member;
  Member registeredMember;
  Wedding wedding;

  @BeforeEach
  void init() {
    member = Member.builder().id(memberId).build();
    wedding = Wedding.builder()
        .id(1L)
        .member(member)
        .weddingDay(LocalDate.now())
        .budget(budget)
        .build();
    registeredMember = Member.builder()
        .id(memberId)
        .wedding(wedding)
        .build();
  }

  @Test
  @DisplayName("현재 예산 조회")
  void getCurrentBudget() {
    // given
    Long payment1 = -2000L;
    Long payment2 = -5000L;

    Transaction transaction1 = Transaction.builder()
        .payment(payment1)
        .build();

    Transaction transaction2 = Transaction.builder()
        .payment(payment2)
        .build();

    given(memberRepository.findById(memberId))
        .willReturn(Optional.ofNullable(registeredMember));
    given(transactionRepository.findByMemberId(memberId))
        .willReturn(List.of(transaction1, transaction2));

    // when
    BudgetDto budgetDto = budgetService.getCurrentBudget(memberId);

    // then
    verify(memberRepository, times(1)).findById(anyLong());
    verify(transactionRepository, times(1)).findByMemberId(anyLong());
    assertEquals(budget + payment1 + payment2, budgetDto.getBudget());
  }

  @Test
  @DisplayName("결혼 정보 등록 전에 현재 예산을 조회하면 IllegalStateException이 발생한다.")
  void throwIllegalStateExceptionBeforeRegisterWedding() {
    // given
    given(memberRepository.findById(memberId))
        .willReturn(Optional.ofNullable(member));

    // when & then
    assertThrows(IllegalStateException.class,
        () -> budgetService.getCurrentBudget(memberId));
  }

  @Test
  @DisplayName("존재하지 않는 회원의 현재 예산을 조회하면 NotFoundException이 발생한다.")
  void throwNotFoundExceptionByNotExistedMember() {
    // when & then
    assertThrows(NotFoundException.class,
        () -> budgetService.getCurrentBudget(memberId));
  }
}
