package com.dnd.weddingmap.domain.transaction.service.impl;

import com.dnd.weddingmap.domain.member.Member;
import com.dnd.weddingmap.domain.transaction.Transaction;
import com.dnd.weddingmap.domain.transaction.dto.TransactionDto;
import com.dnd.weddingmap.domain.transaction.repository.TransactionRepository;
import com.dnd.weddingmap.domain.transaction.service.TransactionService;
import jakarta.transaction.Transactional;
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
}
