package com.dnd.weddingmap.domain.transaction.service;

import com.dnd.weddingmap.domain.member.Member;
import com.dnd.weddingmap.domain.transaction.Transaction;
import com.dnd.weddingmap.domain.transaction.dto.TransactionDto;
import com.dnd.weddingmap.domain.transaction.dto.TransactionListResponseDto;
import java.util.List;

public interface TransactionService {

  TransactionDto createTransaction(TransactionDto dto, Member member);

  Transaction findTransaction(Long transactionId, Long memberId);

  TransactionDto modifyTransaction(Long id, TransactionDto transactionDto);

  boolean withdrawTransaction(Long id);

  List<TransactionListResponseDto> findTransactionList(Long memberId);
}
