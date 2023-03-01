package com.dnd.weddingmap.domain.transaction;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import com.dnd.weddingmap.domain.transaction.dto.TransactionDto;
import java.time.LocalDate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class TransactionTest {

  private final Transaction transaction = Transaction.builder()
      .title("title")
      .agency("agency")
      .transactionDate(LocalDate.now())
      .payment(-100000L)
      .balance(100000L)
      .paymentType(PaymentType.CASH)
      .accountHolder("accountHolder")
      .accountNumber("123-123-123-123")
      .memo("memo")
      .build();

  @Test
  @DisplayName("거래 내역 아이디 조회")
  void getId() {
    assertNull(transaction.getId());
  }

  @Test
  @DisplayName("거래 내역 제목 조회")
  void getTitle() {
    assertEquals("title", transaction.getTitle());
  }

  @Test
  @DisplayName("거래 업체 조회")
  void getAgency() {
    assertEquals("agency", transaction.getAgency());
  }

  @Test
  @DisplayName("거래 날짜 조회")
  void getTransactionDate() {
    assertEquals(LocalDate.now(), transaction.getTransactionDate());
  }

  @Test
  @DisplayName("계약금 조회")
  void getPayment() {
    assertEquals(-100000L, transaction.getPayment());
  }

  @Test
  @DisplayName("잔금 조회")
  void getBalance() {
    assertEquals(100000L, transaction.getBalance());
  }

  @Test
  @DisplayName("거래 형태 조회")
  void getPaymentType() {
    assertEquals(PaymentType.CASH, transaction.getPaymentType());
  }

  @Test
  @DisplayName("거래 계좌 예금주 조회")
  void getAccountHolder() {
    assertEquals("accountHolder", transaction.getAccountHolder());
  }

  @Test
  @DisplayName("거래 계좌 조회")
  void getAccountNumber() {
    assertEquals("123-123-123-123", transaction.getAccountNumber());
  }

  @Test
  @DisplayName("메모 조회")
  void getMemo() {
    assertEquals("memo", transaction.getMemo());
  }

  @Test
  @DisplayName("BaseTimeEntity 생성 시간 조회")
  void getCreatedAt() {
    assertNull(transaction.getCreatedAt());
  }

  @Test
  @DisplayName("BaseTimeEntity 수정 시간 조회")
  void getModifiedAt() {
    assertNull(transaction.getModifiedAt());
  }

  @Test
  @DisplayName("거래 내역 수정")
  void update() {
    String title = "test title";
    String agency = "test agency";
    LocalDate transactionDate = LocalDate.of(2022, 10, 10);
    Long payment = -200000L;
    Long balance = 100000L;
    PaymentType paymentType = PaymentType.CASH;
    String accountHolder = "test accountHolder";
    String accountNumber = "456-456-456-456";
    String memo = "test memo";

    transaction.update(TransactionDto.builder()
        .title(title)
        .agency(agency)
        .transactionDate(transactionDate)
        .payment(payment)
        .balance(balance)
        .paymentType(paymentType)
        .accountHolder(accountHolder)
        .accountNumber(accountNumber)
        .memo(memo)
        .build()
    );

    assertEquals(title, transaction.getTitle());
    assertEquals(agency, transaction.getAgency());
    assertEquals(transactionDate, transaction.getTransactionDate());
    assertEquals(payment, transaction.getPayment());
    assertEquals(balance, transaction.getBalance());
    assertEquals(paymentType, transaction.getPaymentType());
    assertEquals(accountHolder, transaction.getAccountHolder());
    assertEquals(accountNumber, transaction.getAccountNumber());
    assertEquals(memo, transaction.getMemo());
  }
}