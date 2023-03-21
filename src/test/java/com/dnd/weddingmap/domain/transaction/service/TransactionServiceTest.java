package com.dnd.weddingmap.domain.transaction.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.dnd.weddingmap.domain.member.Member;
import com.dnd.weddingmap.domain.member.Role;
import com.dnd.weddingmap.domain.oauth.OAuth2Provider;
import com.dnd.weddingmap.domain.transaction.PaymentType;
import com.dnd.weddingmap.domain.transaction.Transaction;
import com.dnd.weddingmap.domain.transaction.dto.TransactionDto;
import com.dnd.weddingmap.domain.transaction.dto.TransactionListResponseDto;
import com.dnd.weddingmap.domain.transaction.repository.TransactionRepository;
import com.dnd.weddingmap.domain.transaction.service.impl.TransactionServiceImpl;
import com.dnd.weddingmap.global.exception.ForbiddenException;
import com.dnd.weddingmap.global.util.MessageUtil;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;

@ExtendWith(MockitoExtension.class)
class TransactionServiceTest {

  @InjectMocks
  TransactionServiceImpl transactionService;
  @Mock
  TransactionRepository transactionRepository;
  @Mock
  MessageSource messageSource;

  Long transactionId = 1L;
  Long memberId = 1L;
  Member member;
  Transaction transaction;
  TransactionDto transactionDto;

  @BeforeEach
  void init() {
    member = Member.builder()
        .id(memberId)
        .name("test")
        .email("test@example.com")
        .profileImage("test.png")
        .role(Role.USER)
        .oauth2Provider(OAuth2Provider.GOOGLE)
        .build();

    transactionDto = TransactionDto.builder()
        .title("test title")
        .agency("test agency")
        .transactionDate(LocalDate.now())
        .payment(-1000000L)
        .balance(2000000L)
        .paymentType(PaymentType.CARD)
        .accountHolder("test accountHolder")
        .accountNumber("123-123-1234")
        .memo("test memo")
        .build();

    transaction = Transaction.builder()
        .id(1L)
        .title("test title")
        .agency("test agency")
        .transactionDate(LocalDate.now())
        .payment(-1000000L)
        .balance(2000000L)
        .paymentType(PaymentType.CARD)
        .accountHolder("test accountHolder")
        .accountNumber("123-123-1234")
        .memo("test memo")
        .member(member)
        .build();

    MessageUtil.setMessageSource(messageSource);
  }

  @Test
  @DisplayName("거래 내역 등록")
  void createTransaction() {
    // given
    given(transactionRepository.save(any())).willReturn(transaction);

    // when
    TransactionDto savedTransaction = transactionService.createTransaction(transactionDto, member);

    // then
    verify(transactionRepository, times(1)).save(any(Transaction.class));

    assertEquals(transactionDto.getTitle(), savedTransaction.getTitle());
    assertEquals(transactionDto.getAgency(), savedTransaction.getAgency());
    assertEquals(transactionDto.getTransactionDate(), savedTransaction.getTransactionDate());
    assertEquals(transactionDto.getPayment(), savedTransaction.getPayment());
    assertEquals(transactionDto.getBalance(), savedTransaction.getBalance());
    assertEquals(transactionDto.getPaymentType(), savedTransaction.getPaymentType());
    assertEquals(transactionDto.getAccountHolder(), savedTransaction.getAccountHolder());
    assertEquals(transactionDto.getAccountNumber(), savedTransaction.getAccountNumber());
    assertEquals(transactionDto.getMemo(), savedTransaction.getMemo());
  }

  @Test
  @DisplayName("거래 내역 수정")
  void modifyTransaction() {
    // given
    TransactionDto requestDto = TransactionDto.builder()
        .title("modified title")
        .agency("modified agency")
        .transactionDate(LocalDate.of(2022, 10, 10))
        .payment(-200000L)
        .balance(3000000L)
        .paymentType(PaymentType.CASH)
        .accountHolder("modified accountHolder")
        .accountNumber("456-456-456")
        .memo("modified memo")
        .build();

    given(transactionRepository.findById(transactionId)).willReturn(
        Optional.ofNullable(transaction));

    // when
    transactionService.modifyTransaction(transactionId, requestDto);

    // then
    Transaction modifiedTransaction = transactionRepository.findById(transactionId).get();
    assertEquals(requestDto.getTitle(), modifiedTransaction.getTitle());
    assertEquals(requestDto.getAgency(), modifiedTransaction.getAgency());
    assertEquals(requestDto.getTransactionDate(), modifiedTransaction.getTransactionDate());
    assertEquals(requestDto.getPayment(), modifiedTransaction.getPayment());
    assertEquals(requestDto.getBalance(), modifiedTransaction.getBalance());
    assertEquals(requestDto.getPaymentType(), modifiedTransaction.getPaymentType());
    assertEquals(requestDto.getAccountHolder(), modifiedTransaction.getAccountHolder());
    assertEquals(requestDto.getAccountNumber(), modifiedTransaction.getAccountNumber());
    assertEquals(requestDto.getMemo(), modifiedTransaction.getMemo());
  }

  @Test
  @DisplayName("거래 내역 조회")
  void getTransaction() {
    // given
    given(transactionRepository.findById(transactionId)).willReturn(
        Optional.ofNullable(transaction));

    // when
    transactionService.findTransaction(transactionId, memberId);

    // then
    verify(transactionRepository, times(1)).findById(anyLong());
  }

  @Test
  @DisplayName("접근 권한이 없는 거래 내역을 조회하면 ForbiddenException이 발생한다.")
  void throwForbiddenExceptionByNotMatchingTransaction() {
    // given
    given(transactionRepository.findById(transactionId)).willReturn(
        Optional.ofNullable(transaction));

    // when & then
    assertThrows(ForbiddenException.class,
        () -> transactionService.findTransaction(transactionId, 2L));
  }

  @Test
  @DisplayName("거래 내역 리스트 조회")
  void getTransactionList() {
    // given
    given(transactionRepository.findByMemberIdOrderByTransactionDate(memberId)).willReturn(
        List.of(transaction));

    // when
    List<TransactionListResponseDto> result = transactionService.findTransactionList(memberId);

    // then
    verify(transactionRepository, times(1)).findByMemberIdOrderByTransactionDate(anyLong());
    assertEquals(1, result.size());
  }

  @Test
  @DisplayName("거래 내역 삭제")
  void withdrawTransaction() {
    // given
    given(transactionRepository.findById(transactionId)).willReturn(
        Optional.ofNullable(transaction));

    // when
    transactionService.withdrawTransaction(transactionId);

    // then
    verify(transactionRepository, times(1)).delete(any(Transaction.class));
  }
}
