package com.dnd.wedding.domain.jwt.service;

import com.dnd.wedding.domain.jwt.JwtTokenProvider;
import com.dnd.wedding.domain.jwt.RefreshToken;
import com.dnd.wedding.domain.jwt.repository.RefreshTokenRedisRepository;
import com.dnd.wedding.domain.oauth.CustomUserDetails;
import com.dnd.wedding.global.config.util.CookieUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Log4j2
@Service
@RequiredArgsConstructor
public class JwtService {

  @Value("${app.auth.token.refresh-cookie-key}")
  private String cookieKey;

  private final RefreshTokenRedisRepository refreshTokenRedisRepository;
  private final JwtTokenProvider tokenProvider;

  public String refreshToken(HttpServletRequest request, HttpServletResponse response,
      String oldAccessToken) {
    String oldRefreshToken = CookieUtil.getCookie(request, cookieKey)
        .map(Cookie::getValue).orElseThrow(() -> new RuntimeException("No Refresh Token Cookie"));

    if (!tokenProvider.validateToken(oldRefreshToken)) {
      log.info("Not Validated Refresh Token.");
    }

    Authentication authentication = tokenProvider.getAuthentication(oldAccessToken);
    CustomUserDetails user = (CustomUserDetails) authentication.getPrincipal();

    Long id = user.getId();
    Optional<RefreshToken> savedToken = refreshTokenRedisRepository.findById(id);

    if (savedToken.isEmpty()) {
      log.info("Not Existed Refresh Token.");
    } else {
      if (!(savedToken.get().getToken().equals(oldRefreshToken))) {
        log.info("Not Validated Refresh Token.");
      }
    }
    
    String accessToken = tokenProvider.createAccessToken(authentication);
    tokenProvider.addRefreshToken(authentication, response);

    return accessToken;
  }
}