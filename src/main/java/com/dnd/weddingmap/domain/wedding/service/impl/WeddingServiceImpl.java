package com.dnd.weddingmap.domain.wedding.service.impl;

import com.dnd.weddingmap.domain.member.Member;
import com.dnd.weddingmap.domain.member.MemberRepository;
import com.dnd.weddingmap.domain.wedding.Wedding;
import com.dnd.weddingmap.domain.wedding.dto.BudgetDto;
import com.dnd.weddingmap.domain.wedding.dto.WeddingDayDto;
import com.dnd.weddingmap.domain.wedding.repository.WeddingRepository;
import com.dnd.weddingmap.domain.wedding.service.WeddingService;
import com.dnd.weddingmap.global.util.MessageUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class WeddingServiceImpl implements WeddingService {

  private final WeddingRepository weddingRepository;
  private final MemberRepository memberRepository;

  @Override
  @Transactional
  public Long registerWedding(Long memberId, WeddingDayDto weddingDayDto) {

    Member member = memberRepository.findById(memberId)
        .orElseThrow(() -> new IllegalArgumentException(
            MessageUtil.getMessage("member.notFound.exception")));

    if (member.getWedding() != null) {
      throw new IllegalStateException(
          MessageUtil.getMessage("wedding.alreadyRegistered.exception"));
    }

    Wedding wedding = weddingRepository.save(
        new Wedding(member, weddingDayDto.getWeddingDay(), weddingDayDto.isPreparing()));
    member.setWedding(wedding);
    memberRepository.save(member);

    return wedding.getId();
  }

  @Override
  @Transactional
  public void modifyWeddingDay(Long memberId, WeddingDayDto weddingDayDto) {

    Member member = memberRepository.findById(memberId)
        .orElseThrow(() -> new IllegalArgumentException(
            MessageUtil.getMessage("member.notFound.exception")));

    if (member.getWedding() == null) {
      throw new IllegalStateException(
          MessageUtil.getMessage("wedding.notRegistered.exception"));
    }

    Wedding wedding = member.getWedding();
    wedding.setWeddingDay(weddingDayDto.getWeddingDay());
    wedding.setPreparing(weddingDayDto.isPreparing());
  }

  @Override
  @Transactional(readOnly = true)
  public WeddingDayDto getWeddingDay(Long memberId) {

    Member member = memberRepository.findById(memberId)
        .orElseThrow(() -> new IllegalArgumentException(
            MessageUtil.getMessage("member.notFound.exception")));

    if (member.getWedding() == null) {
      throw new IllegalStateException(
          MessageUtil.getMessage("wedding.notRegistered.exception"));
    }

    return WeddingDayDto.builder()
        .weddingDay(member.getWedding().getWeddingDay())
        .preparing(member.getWedding().isPreparing())
        .build();
  }

  @Override
  @Transactional
  public void modifyBudget(Long memberId, BudgetDto budgetDto) {

    Member member = memberRepository.findById(memberId)
        .orElseThrow(() -> new IllegalArgumentException(
            MessageUtil.getMessage("member.notFound.exception")));

    if (member.getWedding() == null) {
      throw new IllegalStateException(
          MessageUtil.getMessage("wedding.notRegistered.exception"));
    }

    Wedding wedding = member.getWedding();
    wedding.setBudget(budgetDto.getBudget());
  }

  @Override
  @Transactional(readOnly = true)
  public BudgetDto getBudget(Long memberId) {

    Member member = memberRepository.findById(memberId)
        .orElseThrow(() -> new IllegalArgumentException(
            MessageUtil.getMessage("member.notFound.exception")));

    if (member.getWedding() == null) {
      throw new IllegalStateException(
          MessageUtil.getMessage("wedding.notRegistered.exception"));
    }

    return BudgetDto.builder()
        .budget(member.getWedding().getBudget())
        .build();
  }
}
