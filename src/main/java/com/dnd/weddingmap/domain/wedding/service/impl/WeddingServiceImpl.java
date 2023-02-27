package com.dnd.weddingmap.domain.wedding.service.impl;

import com.dnd.weddingmap.domain.member.Member;
import com.dnd.weddingmap.domain.member.MemberRepository;
import com.dnd.weddingmap.domain.wedding.Wedding;
import com.dnd.weddingmap.domain.wedding.dto.WeddingDayDto;
import com.dnd.weddingmap.domain.wedding.repository.WeddingRepository;
import com.dnd.weddingmap.domain.wedding.service.WeddingService;
import jakarta.transaction.Transactional;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WeddingServiceImpl implements WeddingService {

  private final WeddingRepository weddingRepository;
  private final MemberRepository memberRepository;

  @Override
  @Transactional
  public Long registerWedding(Member member, WeddingDayDto weddingDayDto) {

    if (member.getWedding() != null) {
      throw new IllegalStateException("이미 예정된 결혼식이 있습니다.");
    }

    Wedding wedding = weddingRepository.save(new Wedding(member, weddingDayDto.getWeddingDay()));
    member.setWedding(wedding);
    memberRepository.save(member);

    return wedding.getId();
  }
}
