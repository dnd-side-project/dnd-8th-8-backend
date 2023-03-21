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
import com.dnd.weddingmap.global.util.MessageUtil;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

  private final TransactionRepository transactionRepository;

  @Override
  @Transactional(rollbackFor = Exception.class)
  public TransactionDto createTransaction(TransactionDto dto, Member member) {
    Transaction savedTransaction = transactionRepository.save(dto.toEntity(member));
    return new TransactionDto(savedTransaction);
  }

  @Override
  @Transactional(readOnly = true)
  public Transaction findTransaction(Long transactionId, Long memberId) {
    Transaction transaction = transactionRepository.findById(transactionId).orElseThrow(
        () -> new NotFoundException(MessageUtil.getMessage("notFound.transaction.exception.msg"))
    );

    if (Objects.equals(transaction.getMember().getId(), memberId)) {
      return transaction;
    } else {
      throw new ForbiddenException(
          MessageUtil.getMessage("inaccessible.transaction.exception.msg"));
    }
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public TransactionDto modifyTransaction(Long id, TransactionDto transactionDto) {
    Transaction transaction = transactionRepository.findById(id).orElseThrow(
        () -> new BadRequestException(
            MessageUtil.getMessage("notFound.transaction.exception.msg")));

    return new TransactionDto(transaction.update(transactionDto));
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public boolean withdrawTransaction(Long id) {
    return transactionRepository.findById(id)
        .map(transaction -> {
          transactionRepository.delete(transaction);
          return true;
        })
        .orElse(false);
  }

  @Override
  @Transactional(readOnly = true)
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
