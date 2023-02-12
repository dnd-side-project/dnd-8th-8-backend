package com.dnd.wedding.domain.member.service;

import com.dnd.wedding.domain.member.Gender;
import java.util.Optional;

public interface MemberService {

  Optional<Gender> getGender(Long id);
}
