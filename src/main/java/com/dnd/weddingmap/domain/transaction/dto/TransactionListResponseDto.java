package com.dnd.weddingmap.domain.transaction.dto;

import com.dnd.weddingmap.domain.transaction.PaymentType;
import java.time.LocalDate;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TransactionListResponseDto {

  private Long id;
  private String title;
  private String agency;
  private LocalDate transactionDate;
  private Long payment;
  private PaymentType paymentType;

  @Builder
  public TransactionListResponseDto(Long id, String title, String agency, LocalDate transactionDate,
      Long payment,
      PaymentType paymentType) {
    this.id = id;
    this.title = title;
    this.agency = agency;
    this.transactionDate = transactionDate;
    this.payment = payment;
    this.paymentType = paymentType;
  }
}
