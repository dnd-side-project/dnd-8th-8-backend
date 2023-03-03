package com.dnd.weddingmap.domain.member.service.impl;

import com.dnd.weddingmap.domain.member.Gender;
import com.dnd.weddingmap.domain.member.Member;
import com.dnd.weddingmap.domain.member.MemberRepository;
import com.dnd.weddingmap.domain.member.dto.NameDto;
import com.dnd.weddingmap.domain.member.service.MemberService;
import jakarta.transaction.Transactional;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

  private final MemberRepository memberRepository;

  @Override
  @Transactional
  public void modifyName(Long id, NameDto nameDto) {
    Optional<Member> member = memberRepository.findById(id);

    member.ifPresent(m -> {
      m.setName(nameDto.getName());
      memberRepository.save(m);
    });
  }

  @Override
  @Transactional
  public Optional<Gender> getGender(Long id) {
    return memberRepository.findById(id).map(Member::getGender);
  }

  @Override
  @Transactional
  public void postGender(Long id, Gender gender) {
    Optional<Member> member = memberRepository.findById(id);

    member.ifPresent(m -> {
      m.setGender(gender);
      memberRepository.save(m);
    });
  }

  @Override
  @Transactional
  public Optional<String> getProfileImage(Long id) {
    return memberRepository.findById(id).map(Member::getProfileImage);
  }

  @Override
  @Transactional
  public void putProfileImage(Long id, String url) {
    Optional<Member> member = memberRepository.findById(id);

    member.ifPresent(m -> {
      m.setProfileImage(url);
      memberRepository.save(m);
    });
  }

  @Override
  @Transactional
  public boolean withdraw(Long id) {
    return memberRepository.findById(id)
        .map(member -> {
          memberRepository.delete(member);
          return true;
        })
        .orElse(false);
  }

  @Override
  @Transactional
  public Optional<Member> findMember(Long id) {
    return memberRepository.findById(id);
  }
}
