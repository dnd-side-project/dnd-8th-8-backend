package com.dnd.weddingmap.domain.transaction.repository;

import com.dnd.weddingmap.domain.transaction.Transaction;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

  List<Transaction> findByMemberIdOrderByTransactionDate(Long id);
}
