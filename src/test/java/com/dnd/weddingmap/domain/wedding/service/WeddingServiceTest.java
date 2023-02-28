package com.dnd.weddingmap.domain.wedding.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import com.dnd.weddingmap.domain.member.Member;
import com.dnd.weddingmap.domain.member.MemberRepository;
import com.dnd.weddingmap.domain.wedding.Wedding;
import com.dnd.weddingmap.domain.wedding.dto.BudgetDto;
import com.dnd.weddingmap.domain.wedding.dto.WeddingDayDto;
import com.dnd.weddingmap.domain.wedding.repository.WeddingRepository;
import com.dnd.weddingmap.domain.wedding.service.impl.WeddingServiceImpl;
import java.time.LocalDate;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class WeddingServiceTest {

  @InjectMocks
  WeddingServiceImpl weddingService;

  @Mock
  WeddingRepository weddingRepository;

  @Mock
  MemberRepository memberRepository;

  Long memberId = 1L;
  Member member;
  Member registeredMember;
  LocalDate weddingDay;
  Wedding wedding;
  WeddingDayDto weddingDayDto;

  @BeforeEach
  void setUp() {
    member = Member.builder().id(memberId).build();
    weddingDay = LocalDate.now();
    wedding = Wedding.builder()
        .id(1L)
        .member(member)
        .weddingDay(weddingDay)
        .build();
    weddingDayDto = WeddingDayDto.builder()
        .weddingDay(weddingDay)
        .build();
    registeredMember = Member.builder()
        .id(1L)
        .wedding(wedding)
        .build();
  }

  @Test
  @DisplayName("결혼식 일정을 등록한다.")
  void registerWedding() {
    // given
    given(weddingRepository.save(any()))
        .willReturn(wedding);
    given(memberRepository.save(any()))
        .willReturn(registeredMember);
    given(memberRepository.findById(memberId))
        .willReturn(Optional.ofNullable(member));
    given(weddingRepository.findById(1L))
        .willReturn(Optional.ofNullable(wedding));

    // when
    Long weddingId = weddingService.registerWedding(memberId, weddingDayDto);

    // then
    Wedding savedWedding = weddingRepository.findById(weddingId).get();
    assertEquals(savedWedding.getWeddingDay(), weddingDay);
    assertEquals(savedWedding.getWeddingMembers().get(0), member);
  }

  @Test
  @DisplayName("결혼식 일정을 수정한다.")
  void modifyWeddingDay() {

    // given
    given(memberRepository.findById(memberId))
        .willReturn(Optional.ofNullable(registeredMember));
    given(weddingRepository.findById(1L))
        .willReturn(Optional.ofNullable(wedding));

    // when
    weddingService.modifyWeddingDay(memberId, weddingDayDto);

    // then
    Wedding savedWedding = weddingRepository.findById(1L).get();
    assertEquals(savedWedding.getWeddingDay(), weddingDay);
  }

  @Test
  @DisplayName("결혼식이 등록되지 않은 경우 결혼식 일정 수정이 불가능하다.")
  void modifyWeddingDayWhenWeddingNotRegistered() {
    // given
    given(memberRepository.findById(memberId))
        .willReturn(Optional.ofNullable(member));

    // then
    assertThrows(IllegalStateException.class,
        () -> weddingService.modifyWeddingDay(memberId, weddingDayDto));
  }

  @Test
  @DisplayName("결혼식 일정을 조회한다.")
  void getWeddingDay() {
    // given
    given(memberRepository.findById(memberId))
        .willReturn(Optional.ofNullable(registeredMember));

    // when
    WeddingDayDto savedWeddingDayDto = weddingService.getWeddingDay(memberId);

    // then
    assertEquals(savedWeddingDayDto.getWeddingDay(), weddingDay);
  }

  @Test
  @DisplayName("결혼 예산을 수정한다.")
  void modifyBudget() {
    // given
    BudgetDto budgetDto = BudgetDto.builder()
        .budget(1000000L)
        .build();

    given(memberRepository.findById(memberId))
        .willReturn(Optional.ofNullable(registeredMember));
    given(weddingRepository.findById(1L))
        .willReturn(Optional.ofNullable(wedding));

    // when
    weddingService.modifyBudget(memberId, budgetDto);

    // then
    Wedding savedWedding = weddingRepository.findById(1L).get();
    assertEquals(savedWedding.getBudget(), budgetDto.getBudget());
  }

  @Test
  @DisplayName("결혼 예산을 조회한다.")
  void getBudget() {
    // given
    given(memberRepository.findById(memberId))
        .willReturn(Optional.ofNullable(registeredMember));

    BudgetDto requestDto = BudgetDto.builder()
        .budget(1000000L)
        .build();
    weddingService.modifyBudget(memberId, requestDto);

    // when

    BudgetDto responseDto = weddingService.getBudget(memberId);

    // then
    assertEquals(responseDto.getBudget(), requestDto.getBudget());
  }
}