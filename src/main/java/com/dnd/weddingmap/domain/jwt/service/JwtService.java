package com.dnd.weddingmap.domain.jwt.service;

import com.dnd.weddingmap.domain.jwt.JwtTokenProvider;
import com.dnd.weddingmap.domain.jwt.RefreshToken;
import com.dnd.weddingmap.domain.jwt.repository.RefreshTokenRedisRepository;
import com.dnd.weddingmap.domain.oauth.CustomUserDetails;
import com.dnd.weddingmap.global.util.CookieUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Optional;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Log4j2
@Service
public class JwtService {

  @Value("${app.auth.token.refresh-cookie-key}")
  private final String cookieKey;

  private final RefreshTokenRedisRepository refreshTokenRedisRepository;
  private final JwtTokenProvider tokenProvider;

  public JwtService(@Value("${app.auth.token.refresh-cookie-key}") String cookieKey,
      RefreshTokenRedisRepository refreshTokenRedisRepository, JwtTokenProvider tokenProvider) {
    this.cookieKey = cookieKey;
    this.refreshTokenRedisRepository = refreshTokenRedisRepository;
    this.tokenProvider = tokenProvider;
  }

  public String refreshToken(HttpServletRequest request, HttpServletResponse response,
      String oldAccessToken) {
    String oldRefreshToken = CookieUtil.getCookie(request, cookieKey)
        .map(Cookie::getValue).orElseThrow(() -> new RuntimeException("No Refresh Token Cookie"));

    if (Boolean.FALSE.equals(tokenProvider.validateToken(oldRefreshToken))) {
      log.info("Not Validated Refresh Token.");
      return null;
    }

    Authentication authentication = tokenProvider.getAuthentication(oldAccessToken);
    CustomUserDetails user = (CustomUserDetails) authentication.getPrincipal();

    Long id = user.getId();
    Optional<RefreshToken> savedToken = refreshTokenRedisRepository.findById(id);

    if (savedToken.isEmpty()) {
      log.info("Not Existed Refresh Token.");
      return null;
    } else {
      if (!(savedToken.get().getToken().equals(oldRefreshToken))) {
        log.info("Not Validated Refresh Token.");
        return null;
      }
    }

    String accessToken = tokenProvider.createAccessToken(authentication);
    tokenProvider.addRefreshToken(authentication, response);

    return accessToken;
  }
}
