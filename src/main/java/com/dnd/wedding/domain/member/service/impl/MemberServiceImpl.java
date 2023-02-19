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

  @Override
  public void postGender(Long id, Gender gender) {
    Optional<Member> member = memberRepository.findById(id);

    member.ifPresent(m -> {
      m.setGender(gender);
      memberRepository.save(m);
    });
  }

  @Override
  public Optional<String> getProfileImage(Long id) {
    return memberRepository.findById(id).map(Member::getProfileImage);
  }

  @Override
  public void putProfileImage(Long id, String url) {
    Optional<Member> member = memberRepository.findById(id);

    member.ifPresent(m -> {
      m.setProfileImage(url);
      memberRepository.save(m);
    });
  }

  @Override
  public boolean withdraw(Long id) {
    return memberRepository.findById(id)
        .map(member -> {
          memberRepository.delete(member);
          return true;
        })
        .orElse(false);
  }

  @Override
  public Optional<Member> findMember(Long id) {
    return memberRepository.findById(id);
  }
}
