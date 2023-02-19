package com.dnd.weddingmap.domain.member.service;

import com.dnd.weddingmap.domain.member.Gender;
import com.dnd.weddingmap.domain.member.Member;
import java.util.Optional;

public interface MemberService {

  Optional<Gender> getGender(Long id);

  void postGender(Long id, Gender gender);

  Optional<String> getProfileImage(Long id);

  void putProfileImage(Long id, String url);

  boolean withdraw(Long id);

  Optional<Member> findMember(Long id);
}
