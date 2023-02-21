package com.dnd.weddingmap.domain.transaction.service;

import com.dnd.weddingmap.domain.member.Member;
import com.dnd.weddingmap.domain.transaction.dto.TransactionDto;

public interface TransactionService {

  TransactionDto createTransaction(TransactionDto dto, Member member);
}
