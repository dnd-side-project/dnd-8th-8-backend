package com.dnd.weddingmap.domain.transaction.service;

import com.dnd.weddingmap.domain.member.Member;
import com.dnd.weddingmap.domain.transaction.Transaction;
import com.dnd.weddingmap.domain.transaction.dto.TransactionDto;
import java.util.Optional;

public interface TransactionService {

  TransactionDto createTransaction(TransactionDto dto, Member member);

  Optional<Transaction> findTransaction(Long id);
}
