package com.dnd.weddingmap.domain.transaction.repository;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.dnd.weddingmap.domain.member.Member;
import com.dnd.weddingmap.domain.member.MemberRepository;
import com.dnd.weddingmap.domain.member.Role;
import com.dnd.weddingmap.domain.oauth.OAuth2Provider;
import com.dnd.weddingmap.domain.transaction.PaymentType;
import com.dnd.weddingmap.domain.transaction.Transaction;
import com.dnd.weddingmap.global.config.JpaConfig;
import jakarta.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;

@Transactional
@DataJpaTest(includeFilters = @ComponentScan.Filter(
    type = FilterType.ASSIGNABLE_TYPE, classes = JpaConfig.class))
class TransactionRepositoryTest {

  @Autowired
  TransactionRepository transactionRepository;
  @Autowired
  MemberRepository memberRepository;
  Member member;

  @BeforeEach
  void init() {
    member = memberRepository.save(Member.builder()
        .name("test1")
        .email("test1@example.com")
        .profileImage("test1.png")
        .role(Role.USER)
        .oauth2Provider(OAuth2Provider.GOOGLE)
        .build());
  }

  @Test
  @DisplayName("사용자 아이디로 거래 내역 조회")
  void findByMemberId() {
    // given
    Transaction transaction1 = Transaction.builder()
        .title("title1")
        .agency("agency1")
        .transactionDate(LocalDate.now())
        .payment(-1000L)
        .balance(1000L)
        .paymentType(PaymentType.CARD)
        .accountHolder("accountHolder1")
        .accountNumber("123-123-123-123")
        .memo("memo")
        .member(member)
        .build();

    Transaction transaction2 = Transaction.builder()
        .title("title2")
        .agency("agency2")
        .transactionDate(LocalDate.now())
        .payment(-1000L)
        .balance(1000L)
        .paymentType(PaymentType.CASH)
        .accountHolder("accountHolder2")
        .accountNumber("123-123-123-123")
        .memo("memo")
        .member(member)
        .build();

    transactionRepository.save(transaction1);
    transactionRepository.save(transaction2);

    // when
    List<Transaction> transactionList = transactionRepository.findByMemberIdOrderByTransactionDate(
        member.getId());

    // then
    assertEquals(2, transactionList.size());
  }

  @Test
  @DisplayName("아이디로 거래 내역 조회")
  void findById() {
    // given
    String title = "test title";
    String agency = "test agency";
    LocalDate transactionDate = LocalDate.now();
    Long payment = -100000L;
    Long balance = 100000L;
    PaymentType paymentType = PaymentType.CARD;
    String accountHolder = "test accountHolder";
    String accountNumber = "123-123-123-123";
    String memo = "test memo";

    Transaction savedTransaction = transactionRepository.save(Transaction.builder()
        .title(title)
        .agency(agency)
        .transactionDate(transactionDate)
        .payment(payment)
        .balance(balance)
        .paymentType(paymentType)
        .accountHolder(accountHolder)
        .accountNumber(accountNumber)
        .memo(memo)
        .member(member)
        .build());

    // when
    Optional<Transaction> transaction = transactionRepository.findById(savedTransaction.getId());

    // then
    assertTrue(transaction.isPresent());
    assertEquals(title, transaction.get().getTitle());
    assertEquals(agency, transaction.get().getAgency());
    assertEquals(transactionDate, transaction.get().getTransactionDate());
    assertEquals(payment, transaction.get().getPayment());
    assertEquals(balance, transaction.get().getBalance());
    assertEquals(paymentType, transaction.get().getPaymentType());
    assertEquals(accountHolder, transaction.get().getAccountHolder());
    assertEquals(accountNumber, transaction.get().getAccountNumber());
    assertEquals(memo, transaction.get().getMemo());
  }
}
