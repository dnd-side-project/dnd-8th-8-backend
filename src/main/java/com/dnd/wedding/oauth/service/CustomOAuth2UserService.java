package com.dnd.wedding.oauth.service;

import com.dnd.wedding.domain.member.Member;
import com.dnd.wedding.domain.member.MemberRepository;
import com.dnd.wedding.domain.member.Role;
import com.dnd.wedding.domain.oauth.OAuth2Provider;
import com.dnd.wedding.domain.oauth.CustomUserDetails;
import com.dnd.wedding.oauth.exception.OAuthProcessingException;
import com.dnd.wedding.oauth.info.OAuth2UserInfo;
import com.dnd.wedding.oauth.info.OAuth2UserInfoFactory;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

  private final MemberRepository memberRepository;

  @Override
  public OAuth2User loadUser(OAuth2UserRequest userRequest)
      throws OAuth2AuthenticationException {
    OAuth2User oauth2User = super.loadUser(userRequest);

    try {
      return processoOAuth2User(userRequest, oauth2User);
    } catch (AuthenticationException e) {
      throw e;
    } catch (Exception e) {
      throw new InternalAuthenticationServiceException(e.getMessage(), e.getCause());
    }
  }

  private OAuth2User processoOAuth2User(OAuth2UserRequest userRequest, OAuth2User oauth2User) {
    OAuth2Provider oauth2Provider = OAuth2Provider.valueOf(
        userRequest.getClientRegistration().getRegistrationId().toUpperCase());
    OAuth2UserInfo userInfo = OAuth2UserInfoFactory.getOAuth2Userinfo(oauth2Provider,
        oauth2User.getAttributes());

    if (userInfo.getEmail().isEmpty()) {
      throw new OAuthProcessingException("Email not found from OAuth2 provider");
    }
    Optional<Member> memberOptional = memberRepository.findByEmail(userInfo.getEmail());
    Member member;

    if (memberOptional.isPresent()) {
      member = memberOptional.get();
      if (oauth2Provider != member.getOauth2Provider()) {
        throw new OAuthProcessingException("Wrong Match Auth Provider");
      }

    } else {
      member = createMember(userInfo, oauth2Provider);
    }
    return CustomUserDetails.create(member, oauth2User.getAttributes());
  }

  private Member createMember(OAuth2UserInfo userInfo, OAuth2Provider oauth2Provider) {
    return memberRepository.save(Member.builder()
        .email(userInfo.getEmail())
        .name(userInfo.getName())
        .profileImage(userInfo.getImageUrl())
        .role(Role.USER)
        .oauth2Provider(oauth2Provider)
        .build());
  }
}
