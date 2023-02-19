package com.dnd.weddingmap.domain.jwt.service;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.dnd.weddingmap.domain.jwt.JwtTokenProvider;
import com.dnd.weddingmap.domain.jwt.RefreshToken;
import com.dnd.weddingmap.domain.jwt.repository.RefreshTokenRedisRepository;
import com.dnd.weddingmap.domain.member.Member;
import com.dnd.weddingmap.domain.member.Role;
import com.dnd.weddingmap.domain.oauth.CustomUserDetails;
import com.dnd.weddingmap.domain.oauth.OAuth2Provider;
import jakarta.servlet.http.Cookie;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

class JwtServiceTest {

  private static final Collection<? extends GrantedAuthority> authority = Collections.singletonList(
      new SimpleGrantedAuthority("ROLE_USER"));

  private final String cookieKey = "testCookieKey";
  private final String oldRefreshToken = "testRefreshToken";
  private final String oldAccessToken = "testAccessToken";
  private final CustomUserDetails customUserDetails = new CustomUserDetails(
      Member.builder()
          .id(1L)
          .email("test@test.com")
          .name("test")
          .role(Role.USER)
          .oauth2Provider(OAuth2Provider.GOOGLE)
          .build());

  private RefreshTokenRedisRepository refreshTokenRedisRepository;
  private JwtTokenProvider tokenProvider;
  private JwtService jwtService;
  private MockHttpServletResponse response;
  private MockHttpServletRequest request;
  private Authentication authentication;

  @BeforeEach
  void init() {
    this.refreshTokenRedisRepository = mock(RefreshTokenRedisRepository.class);
    this.tokenProvider = mock(JwtTokenProvider.class);
    this.authentication = mock(Authentication.class);
    this.jwtService = new JwtService(cookieKey, refreshTokenRedisRepository, tokenProvider);
    this.request = new MockHttpServletRequest();
    this.response = new MockHttpServletResponse();
  }

  @Test
  @DisplayName("쿠키에 refresh token이 없는 경우 에러가 발생한다.")
  void throwException() {
    assertThrows(RuntimeException.class, () -> jwtService.refreshToken(request, response, "token"));
  }

  @Test
  @DisplayName("accessToken 재발급 성공")
  void suceessRenewToken() {
    Cookie cookie = new Cookie(cookieKey, oldRefreshToken);
    request.setCookies(cookie);

    RefreshToken refreshToken = RefreshToken.builder()
        .id(1L)
        .token(oldRefreshToken)
        .build();
    when(tokenProvider.validateToken(oldRefreshToken)).thenReturn(true);
    when(tokenProvider.getAuthentication(oldAccessToken)).thenReturn(authentication);
    when(authentication.getPrincipal()).thenReturn(customUserDetails);
    when(refreshTokenRedisRepository.findById(1L)).thenReturn(Optional.of(refreshToken));

    jwtService.refreshToken(request, response, oldAccessToken);
    verify(tokenProvider).createAccessToken(authentication);
    verify(tokenProvider).addRefreshToken(authentication, response);
  }

  @Test
  @DisplayName("refresh token이 유효하지 않은 경우 null을 반환한다.")
  void failRenewTokenByInvalidRefreshToken() {
    Cookie cookie = new Cookie(cookieKey, oldRefreshToken);
    request.setCookies(cookie);

    String token = jwtService.refreshToken(request, response, oldAccessToken);

    assertNull(token);
  }

  @Test
  @DisplayName("저장된 refresh token이 없는 경우 null을 반환한다.")
  void failRenewTokenByNotSavedRefreshToken() {
    Cookie cookie = new Cookie(cookieKey, oldRefreshToken);
    request.setCookies(cookie);

    when(tokenProvider.validateToken(oldRefreshToken)).thenReturn(true);
    when(tokenProvider.getAuthentication(oldAccessToken)).thenReturn(authentication);
    when(authentication.getPrincipal()).thenReturn(customUserDetails);

    String token = jwtService.refreshToken(request, response, oldAccessToken);

    assertNull(token);
  }

  @Test
  @DisplayName("쿠키의 refresh token이 저장된 refresh token과 일치하지 않는 경우 null을 반환한다.")
  void failRenewTokenByDifferentRefreshToken() {
    Cookie cookie = new Cookie(cookieKey, oldRefreshToken);
    request.setCookies(cookie);

    RefreshToken refreshToken = RefreshToken.builder()
        .id(1L)
        .token("test")
        .build();

    when(tokenProvider.validateToken(oldRefreshToken)).thenReturn(true);
    when(tokenProvider.getAuthentication(oldAccessToken)).thenReturn(authentication);
    when(authentication.getPrincipal()).thenReturn(customUserDetails);
    when(refreshTokenRedisRepository.findById(1L)).thenReturn(Optional.of(refreshToken));

    String token = jwtService.refreshToken(request, response, oldAccessToken);

    assertNull(token);
  }
}
