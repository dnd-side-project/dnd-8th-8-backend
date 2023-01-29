package com.dnd.wedding.domain.oauth.handler;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.dnd.wedding.domain.jwt.JwtTokenProvider;
import com.dnd.wedding.domain.jwt.repository.CookieAuthorizationRequestRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;

class OAuth2AuthenticationSuccessHandlerTest {

  private final HttpServletResponse response = mock(HttpServletResponse.class);
  private final HttpServletRequest request = mock(HttpServletRequest.class);
  private final JwtTokenProvider tokenProvider = mock(JwtTokenProvider.class);
  private final Authentication authentication = mock(Authentication.class);
  private final CookieAuthorizationRequestRepository authorizationRequestRepository =
      mock(CookieAuthorizationRequestRepository.class);
  private OAuth2AuthenticationSuccessHandler oauth2AuthenticationSuccessHandler;

  @BeforeEach
  void init() {
    oauth2AuthenticationSuccessHandler = new OAuth2AuthenticationSuccessHandler(tokenProvider,
        authorizationRequestRepository);

    MockitoAnnotations.openMocks(this);
  }


  @Test
  @DisplayName("OnAuthenticationSuccess 정상 호출된 경우")
  void onAuthenticationSuccess() throws IOException {
    when(tokenProvider.createAccessToken(authentication)).thenReturn("token");
    doNothing().when(authorizationRequestRepository)
        .removeAuthorizationRequestCookies(request, response);
    when(response.isCommitted()).thenReturn(false);

    oauth2AuthenticationSuccessHandler.onAuthenticationSuccess(request, response, authentication);

    verify(tokenProvider).createAccessToken(authentication);
    verify(tokenProvider).createRefreshToken(authentication, response);
    verify(response).isCommitted();
    verify(authorizationRequestRepository).removeAuthorizationRequestCookies(request, response);
  }

  @Test
  @DisplayName("http 응답이 클라이언트로 이미 전달된 경우")
  void responseCommitted() throws IOException {
    when(tokenProvider.createAccessToken(authentication)).thenReturn("token");
    when(response.isCommitted()).thenReturn(true);

    oauth2AuthenticationSuccessHandler.onAuthenticationSuccess(request, response, authentication);

    verify(tokenProvider).createAccessToken(authentication);
    verify(tokenProvider).createRefreshToken(authentication, response);
    verify(response).isCommitted();
  }
}