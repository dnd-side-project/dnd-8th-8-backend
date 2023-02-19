package com.dnd.wedding.domain.member.service;

import com.dnd.wedding.domain.member.Gender;
import java.util.Optional;

public interface MemberService {

  Optional<Gender> getGender(Long id);

  void postGender(Long id, Gender gender);

  Optional<String> getProfileImage(Long id);

  void putProfileImage(Long id, String url);

  boolean withdraw(Long id);
}
