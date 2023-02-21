package com.dnd.weddingmap.domain.transaction.service.impl;

import com.dnd.weddingmap.domain.member.Member;
import com.dnd.weddingmap.domain.transaction.Transaction;
import com.dnd.weddingmap.domain.transaction.dto.TransactionDto;
import com.dnd.weddingmap.domain.transaction.repository.TransactionRepository;
import com.dnd.weddingmap.domain.transaction.service.TransactionService;
import com.dnd.weddingmap.global.exception.BadRequestException;
import jakarta.transaction.Transactional;
import java.util.Optional;
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
  public Optional<Transaction> findTransaction(Long id) {
    return transactionRepository.findById(id);
  }

  @Transactional
  @Override
  public TransactionDto modifyTransaction(Long id, TransactionDto transactionDto) {
    Transaction transaction = transactionRepository.findById(id).orElseThrow(
        () -> new BadRequestException("존재하지 않는 예산표입니다."));

    return new TransactionDto(transaction.update(transactionDto));
  }
}
