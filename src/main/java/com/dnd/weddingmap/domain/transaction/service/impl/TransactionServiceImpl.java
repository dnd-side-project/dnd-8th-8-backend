package com.dnd.weddingmap.domain.transaction.service.impl;

import com.dnd.weddingmap.domain.member.Member;
import com.dnd.weddingmap.domain.transaction.Transaction;
import com.dnd.weddingmap.domain.transaction.dto.TransactionDto;
import com.dnd.weddingmap.domain.transaction.dto.TransactionListResponseDto;
import com.dnd.weddingmap.domain.transaction.repository.TransactionRepository;
import com.dnd.weddingmap.domain.transaction.service.TransactionService;
import com.dnd.weddingmap.global.exception.BadRequestException;
import com.dnd.weddingmap.global.exception.ForbiddenException;
import com.dnd.weddingmap.global.exception.NotFoundException;
import jakarta.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class TransactionServiceImpl implements TransactionService {

  private final TransactionRepository transactionRepository;

  @Transactional
  @Override
  public TransactionDto createTransaction(TransactionDto dto, Member member) {
    Transaction savedTransaction = transactionRepository.save(dto.toEntity(member));
    return new TransactionDto(savedTransaction);
  }

  @Transactional
  @Override
  public Transaction findTransaction(Long transactionId, Long memberId) {
    Transaction transaction = transactionRepository.findById(transactionId).orElseThrow(
        () -> new NotFoundException("존재하지 않는 예산표입니다.")
    );

    if (Objects.equals(transaction.getMember().getId(), memberId)) {
      return transaction;
    } else {
      throw new ForbiddenException("접근할 수 없는 예산표입니다.");
    }
  }

  @Transactional
  @Override
  public TransactionDto modifyTransaction(Long id, TransactionDto transactionDto) {
    Transaction transaction = transactionRepository.findById(id).orElseThrow(
        () -> new BadRequestException("존재하지 않는 예산표입니다."));

    return new TransactionDto(transaction.update(transactionDto));
  }

  @Transactional
  @Override
  public boolean withdrawTransaction(Long id) {
    return transactionRepository.findById(id)
        .map(transaction -> {
          transactionRepository.delete(transaction);
          return true;
        })
        .orElse(false);
  }

  @Transactional
  @Override
  public List<TransactionListResponseDto> findTransactionList(Long memberId) {
    List<Transaction> transactionList =
        transactionRepository.findByMemberIdOrderByTransactionDate(memberId);

    List<TransactionListResponseDto> result = new ArrayList<>();
    transactionList.forEach(transaction -> result.add(TransactionListResponseDto.builder()
        .id(transaction.getId())
        .title(transaction.getTitle())
        .agency(transaction.getAgency())
        .transactionDate(transaction.getTransactionDate())
        .payment(transaction.getPayment())
        .paymentType(transaction.getPaymentType())
        .build()));
    return result;
  }
}
