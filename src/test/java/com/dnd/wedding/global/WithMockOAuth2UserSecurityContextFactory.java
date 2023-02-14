package com.dnd.wedding.global;

import com.dnd.wedding.domain.member.Member;
import com.dnd.wedding.domain.member.Role;
import com.dnd.wedding.domain.oauth.CustomUserDetails;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

public class WithMockOAuth2UserSecurityContextFactory implements
    WithSecurityContextFactory<WithMockOAuth2User> {

  @Override
  public SecurityContext createSecurityContext(WithMockOAuth2User annotation) {
    SecurityContext context = SecurityContextHolder.createEmptyContext();

    CustomUserDetails principal =
        new CustomUserDetails(Member.builder()
            .id(1L)
            .name("testName")
            .email("test@test.com")
            .profileImage("https://test_profile_image")
            .role(Role.USER)
            .build());

    Authentication auth =
        new UsernamePasswordAuthenticationToken(principal, "password", principal.getAuthorities());
    context.setAuthentication(auth);
    return context;
  }
}

