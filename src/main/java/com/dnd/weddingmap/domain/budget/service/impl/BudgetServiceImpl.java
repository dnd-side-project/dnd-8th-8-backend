package com.dnd.weddingmap.domain.budget.service.impl;

import com.dnd.weddingmap.domain.budget.service.BudgetService;
import com.dnd.weddingmap.domain.member.Member;
import com.dnd.weddingmap.domain.member.MemberRepository;
import com.dnd.weddingmap.domain.transaction.Transaction;
import com.dnd.weddingmap.domain.transaction.repository.TransactionRepository;
import com.dnd.weddingmap.domain.wedding.dto.BudgetDto;
import com.dnd.weddingmap.global.exception.NotFoundException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BudgetServiceImpl implements BudgetService {

  private final MemberRepository memberRepository;
  private final TransactionRepository transactionRepository;

  @Override
  @Transactional(readOnly = true)
  public BudgetDto getCurrentBudget(Long memberId) {
    Member member = memberRepository.findById(memberId)
        .orElseThrow(() -> new NotFoundException("존재하지 않는 회원입니다."));

    if (member.getWedding() == null) {
      throw new IllegalStateException("결혼이 등록되어 있지 않습니다.");
    }

    Long budget = member.getWedding().getBudget();
    Long totalPayment = 0L;

    List<Transaction> transactionList = transactionRepository.findByMemberId(memberId);
    for (Transaction transaction : transactionList) {
      totalPayment += transaction.getPayment();
    }

    return BudgetDto.builder()
        .budget(budget + totalPayment)
        .build();
  }
}
