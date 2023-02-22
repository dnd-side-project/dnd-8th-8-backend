package com.dnd.weddingmap.domain.jwt;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import java.io.IOException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

class JwtAuthenticationFilterTest {

  private static final String TOKEN_VALUE = "accessToken";
  private static final String BEARER = "Bearer ";
  @InjectMocks
  private JwtAuthenticationFilter jwtAuthenticationFilter;
  private MockHttpServletRequest request;
  private MockHttpServletResponse response;
  private FilterChain chain;
  private JwtTokenProvider tokenProvider;

  @BeforeEach
  void init() {
    this.request = new MockHttpServletRequest();
    this.response = new MockHttpServletResponse();
    this.chain = mock(FilterChain.class);

    tokenProvider = mock(JwtTokenProvider.class);
    this.jwtAuthenticationFilter = new JwtAuthenticationFilter(tokenProvider);
  }

  @AfterEach
  void after() {
    SecurityContextHolder.getContext().setAuthentication(null);
  }

  @Test
  @DisplayName("유효한 토큰이 있는 경우 인증 객체가 등록된다.")
  void setAuthenticationWithValidToken() throws ServletException, IOException {
    when(tokenProvider.validateToken(TOKEN_VALUE)).thenReturn(true);
    when(tokenProvider.getAuthentication(TOKEN_VALUE)).thenReturn(mock(Authentication.class));

    request.addHeader("Authorization", BEARER + TOKEN_VALUE);
    jwtAuthenticationFilter.doFilterInternal(request, response, chain);

    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    assertThat(authentication).isNotNull();

    verify(chain, times(1)).doFilter(request, response);
  }

  @Test
  @DisplayName("유효한 토큰이 없는 경우 인증 객체 등록에 실패한다.")
  void failSetAuthenticationWithInvalidToken() throws ServletException, IOException {
    when(tokenProvider.validateToken(TOKEN_VALUE)).thenReturn(false);

    jwtAuthenticationFilter.doFilterInternal(request, response, chain);

    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    assertThat(authentication).isNull();
  }
}
