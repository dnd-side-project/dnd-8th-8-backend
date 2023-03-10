package com.dnd.weddingmap.domain.transaction.dto;

import com.dnd.weddingmap.domain.member.Member;
import com.dnd.weddingmap.domain.transaction.PaymentType;
import com.dnd.weddingmap.domain.transaction.Transaction;
import com.dnd.weddingmap.global.validator.ValidEnum;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class TransactionDto {

  private Long id;
  @NotBlank
  private String title;
  @NotBlank
  private String agency;
  @NotNull
  private LocalDate transactionDate;
  @NotNull
  private Long payment;
  private Long balance;
  @ValidEnum(value = PaymentType.class, message = "거래 구분은 CASH, CARD만 등록 가능합니다.")
  private PaymentType paymentType;
  private String accountHolder;
  private String accountNumber;
  private String memo;

  @Builder
  public TransactionDto(String title, String agency, LocalDate transactionDate, Long payment,
      Long balance, PaymentType paymentType, String accountHolder,
      String accountNumber, String memo) {
    this.title = title;
    this.agency = agency;
    this.transactionDate = transactionDate;
    this.payment = payment;
    this.balance = balance;
    this.paymentType = paymentType;
    this.accountHolder = accountHolder;
    this.accountNumber = accountNumber;
    this.memo = memo;
  }

  public TransactionDto(Transaction transaction) {
    this.id = transaction.getId();
    this.title = transaction.getTitle();
    this.agency = transaction.getAgency();
    this.transactionDate = transaction.getTransactionDate();
    this.payment = transaction.getPayment();
    this.balance = transaction.getBalance();
    this.paymentType = transaction.getPaymentType();
    this.accountHolder = transaction.getAccountHolder();
    this.accountNumber = transaction.getAccountNumber();
    this.memo = transaction.getMemo();
  }

  public Transaction toEntity(Member member) {
    return Transaction.builder()
        .id(this.id)
        .title(this.title)
        .agency(this.agency)
        .transactionDate(this.transactionDate)
        .payment(this.payment)
        .balance(this.balance)
        .paymentType(this.paymentType)
        .accountHolder(this.accountHolder)
        .accountNumber(this.accountNumber)
        .memo(this.memo)
        .member(member)
        .build();
  }
}
