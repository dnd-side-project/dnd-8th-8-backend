package com.dnd.weddingmap.domain.transaction;

import com.dnd.weddingmap.domain.common.BaseTimeEntity;
import com.dnd.weddingmap.domain.member.Member;
import com.dnd.weddingmap.domain.transaction.dto.TransactionDto;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.time.LocalDate;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Entity
@Getter
@RequiredArgsConstructor
public class Transaction extends BaseTimeEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(length = 64, nullable = false)
  private String title;

  @Column(length = 64, nullable = false)
  private String agency;

  @Column(nullable = false)
  private LocalDate transactionDate;

  @Column(nullable = false)
  private Long payment;

  @Column
  private Long balance;

  @Column(nullable = false)
  @Enumerated(value = EnumType.STRING)
  private PaymentType paymentType;

  @Column
  private String accountHolder;

  @Column
  private String accountNumber;

  @Column
  private String memo;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "memberId", nullable = false)
  private Member member;

  @Builder
  public Transaction(Long id, String title, String agency, LocalDate transactionDate, Long payment,
      Long balance, PaymentType paymentType, String accountHolder,
      String accountNumber,
      String memo,
      Member member) {
    this.id = id;
    this.title = title;
    this.agency = agency;
    this.transactionDate = transactionDate;
    this.payment = payment;
    this.balance = balance;
    this.paymentType = paymentType;
    this.accountHolder = accountHolder;
    this.accountNumber = accountNumber;
    this.memo = memo;
    this.member = member;
  }

  public Transaction update(TransactionDto transactionDto) {
    this.title = transactionDto.getTitle();
    this.agency = transactionDto.getAgency();
    this.transactionDate = transactionDto.getTransactionDate();
    this.payment = transactionDto.getPayment();
    this.balance = transactionDto.getBalance();
    this.paymentType = transactionDto.getPaymentType();
    this.accountHolder = transactionDto.getAccountHolder();
    this.accountNumber = transactionDto.getAccountNumber();
    this.memo = transactionDto.getMemo();

    return this;
  }
}

