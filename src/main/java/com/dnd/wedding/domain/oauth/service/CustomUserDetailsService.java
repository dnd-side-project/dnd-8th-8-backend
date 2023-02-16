package com.dnd.wedding.domain.oauth.service;

import com.dnd.wedding.domain.member.Member;
import com.dnd.wedding.domain.member.MemberRepository;
import com.dnd.wedding.domain.oauth.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

  private final MemberRepository memberRepository;

  @Override
  public UserDetails loadUserByUsername(String id) throws UsernameNotFoundException {
    Member member = memberRepository.findById(Long.valueOf(id))
        .orElseThrow(() -> new UsernameNotFoundException("User Not Found with id: " + id));
    return new CustomUserDetails(member);
  }
}
