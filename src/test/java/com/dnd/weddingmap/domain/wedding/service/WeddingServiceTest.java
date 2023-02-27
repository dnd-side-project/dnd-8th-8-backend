package com.dnd.weddingmap.domain.wedding.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import com.dnd.weddingmap.domain.member.Member;
import com.dnd.weddingmap.domain.member.MemberRepository;
import com.dnd.weddingmap.domain.wedding.Wedding;
import com.dnd.weddingmap.domain.wedding.dto.WeddingDayDto;
import com.dnd.weddingmap.domain.wedding.repository.WeddingRepository;
import com.dnd.weddingmap.domain.wedding.service.impl.WeddingServiceImpl;
import java.time.LocalDate;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
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

  Member member;
  Member registeredMember;
  LocalDate weddingDay;
  Wedding wedding;
  WeddingDayDto weddingDayDto;

  @BeforeEach
  void setUp() {
    member = Member.builder().id(1L).build();
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
  void registerWedding() {
    // given
    given(weddingRepository.save(any()))
        .willReturn(wedding);
    given(memberRepository.save(any()))
        .willReturn(registeredMember);
    given(weddingRepository.findById(1L))
        .willReturn(Optional.ofNullable(wedding));

    // when
    Long weddingId = weddingService.registerWedding(member, weddingDayDto);

    // then
    Wedding savedWedding = weddingRepository.findById(weddingId).get();
    assertEquals(savedWedding.getWeddingDay(), weddingDay);
    assertEquals(savedWedding.getWeddingMembers().get(0), member);
  }
}