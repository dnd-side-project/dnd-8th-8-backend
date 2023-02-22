package com.dnd.weddingmap.domain.transaction.dto;

import com.dnd.weddingmap.domain.transaction.TransactionCategory;
import java.time.LocalDate;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class TransactionListResponseDto {

  private Long id;
  private String title;
  private String agency;
  private LocalDate transactionDate;
  private Long payment;
  private TransactionCategory transactionCategory;

  @Builder
  public TransactionListResponseDto(Long id, String title, String agency, LocalDate transactionDate,
      Long payment,
      TransactionCategory transactionCategory) {
    this.id = id;
    this.title = title;
    this.agency = agency;
    this.transactionDate = transactionDate;
    this.payment = payment;
    this.transactionCategory = transactionCategory;
  }
}
