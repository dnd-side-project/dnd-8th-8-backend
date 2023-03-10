package com.dnd.weddingmap.domain.oauth.handler;


import static com.dnd.weddingmap.domain.jwt.repository.CookieAuthorizationRequestRepository.REDIRECT_URI_PARAM_COOKIE_NAME;

import com.dnd.weddingmap.domain.jwt.JwtTokenProvider;
import com.dnd.weddingmap.domain.jwt.repository.CookieAuthorizationRequestRepository;
import com.dnd.weddingmap.global.exception.BadRequestException;
import com.dnd.weddingmap.global.util.CookieUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URI;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

@Component
@RequiredArgsConstructor
@Slf4j
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

  @Value("${app.oauth2.authorized-redirect-uri}")
  private String redirectUri;

  private final JwtTokenProvider tokenProvider;
  private final CookieAuthorizationRequestRepository authorizationRequestRepository;

  @Override
  public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
      Authentication authentication) throws IOException {
    String targetUrl = determineTargetUrl(request, response, authentication);

    if (response.isCommitted()) {
      log.debug("Response has already been committed. Unable to redirect to " + targetUrl);
      return;
    }
    clearAuthenticationAttributes(request, response);
    getRedirectStrategy().sendRedirect(request, response, targetUrl);
  }

  @Override
  protected String determineTargetUrl(HttpServletRequest request, HttpServletResponse response,
      Authentication authentication) {
    Optional<String> redirect = CookieUtil.getCookie(request, REDIRECT_URI_PARAM_COOKIE_NAME)
        .map(Cookie::getValue);

    if (redirect.isPresent() && !isAuthorizedRedirectUri(redirect.get())) {
      throw new BadRequestException("redirect URIs are not matched");
    }
    String targetUrl = redirect.orElse(getDefaultTargetUrl());

    // jwt
    String accessToken = tokenProvider.createAccessToken(authentication);
    tokenProvider.addRefreshToken(authentication, response);

    return UriComponentsBuilder.fromUriString(targetUrl)
        .queryParam("code", accessToken)
        .build().toUriString();
  }

  protected void clearAuthenticationAttributes(HttpServletRequest request,
      HttpServletResponse response) {
    super.clearAuthenticationAttributes(request);
    authorizationRequestRepository.removeAuthorizationRequestCookies(request, response);
  }

  private boolean isAuthorizedRedirectUri(String uri) {
    URI clientRedirectUri = URI.create(uri);
    URI authorizedUri = URI.create(redirectUri);

    return authorizedUri.getHost().equalsIgnoreCase(clientRedirectUri.getHost())
        && authorizedUri.getPort() == clientRedirectUri.getPort();
  }
}
