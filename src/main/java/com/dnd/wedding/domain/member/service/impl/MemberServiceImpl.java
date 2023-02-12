package com.dnd.wedding.domain.member.service.impl;

import com.dnd.wedding.domain.member.Gender;
import com.dnd.wedding.domain.member.Member;
import com.dnd.wedding.domain.member.MemberRepository;
import com.dnd.wedding.domain.member.service.MemberService;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

  private final MemberRepository memberRepository;

  @Override
  public Optional<Gender> getGender(Long id) {
    return memberRepository.findById(id).map(Member::getGender);
  }
}
