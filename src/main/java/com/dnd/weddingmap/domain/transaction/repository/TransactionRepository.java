package com.dnd.weddingmap.domain.transaction.repository;

import com.dnd.weddingmap.domain.transaction.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

}
