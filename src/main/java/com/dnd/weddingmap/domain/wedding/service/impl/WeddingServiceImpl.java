package com.dnd.weddingmap.domain.wedding.service.impl;

import com.dnd.weddingmap.domain.member.Member;
import com.dnd.weddingmap.domain.member.MemberRepository;
import com.dnd.weddingmap.domain.wedding.Wedding;
import com.dnd.weddingmap.domain.wedding.dto.BudgetDto;
import com.dnd.weddingmap.domain.wedding.dto.WeddingDayDto;
import com.dnd.weddingmap.domain.wedding.repository.WeddingRepository;
import com.dnd.weddingmap.domain.wedding.service.WeddingService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WeddingServiceImpl implements WeddingService {

  private final WeddingRepository weddingRepository;
  private final MemberRepository memberRepository;

  @Override
  @Transactional
  public Long registerWedding(Long memberId, WeddingDayDto weddingDayDto) {

    Member member = memberRepository.findById(memberId)
        .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));

    if (member.getWedding() != null) {
      throw new IllegalStateException("결혼이 이미 등록되어 있습니다.");
    }

    Wedding wedding = weddingRepository.save(new Wedding(member, weddingDayDto.getWeddingDay()));
    member.setWedding(wedding);
    memberRepository.save(member);

    return wedding.getId();
  }

  @Override
  @Transactional
  public void modifyWeddingDay(Long memberId, WeddingDayDto weddingDayDto) {

    Member member = memberRepository.findById(memberId)
        .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));

    if (member.getWedding() == null) {
      throw new IllegalStateException("결혼이 등록되어 있지 않습니다.");
    }

    Wedding wedding = member.getWedding();
    wedding.setWeddingDay(weddingDayDto.getWeddingDay());
    weddingRepository.save(wedding);
  }

  @Override
  @Transactional
  public WeddingDayDto getWeddingDay(Long memberId) {

    Member member = memberRepository.findById(memberId)
        .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));

    if (member.getWedding() == null) {
      throw new IllegalStateException("결혼이 등록되어 있지 않습니다.");
    }

    return WeddingDayDto.builder()
        .weddingDay(member.getWedding().getWeddingDay())
        .build();
  }

  @Override
  @Transactional
  public void modifyBudget(Long memberId, BudgetDto budgetDto) {

    Member member = memberRepository.findById(memberId)
        .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));

    if (member.getWedding() == null) {
      throw new IllegalStateException("결혼이 등록되어 있지 않습니다.");
    }

    Wedding wedding = member.getWedding();
    wedding.setBudget(budgetDto.getBudget());
    weddingRepository.save(wedding);
  }

  @Override
  @Transactional
  public BudgetDto getBudget(Long memberId) {

    Member member = memberRepository.findById(memberId)
        .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));

    if (member.getWedding() == null) {
      throw new IllegalStateException("결혼이 등록되어 있지 않습니다.");
    }

    return BudgetDto.builder()
        .budget(member.getWedding().getBudget())
        .build();
  }
}
