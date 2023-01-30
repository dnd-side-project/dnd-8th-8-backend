package com.dnd.wedding.domain.jwt;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import com.dnd.wedding.domain.jwt.repository.RefreshTokenRedisRepository;
import com.dnd.wedding.domain.member.Role;
import com.dnd.wedding.domain.oauth.CustomUserDetails;
import com.dnd.wedding.domain.oauth.OAuth2Provider;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Collections;
import javax.crypto.SecretKey;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseCookie;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

class JwtTokenProviderTest {

  private static final String SECRET =
      "testSecretKeytestSecretKeytestSecretKeytestSecretKeytestSecretKeytestSecretKeytestSecretKey";
  private static final Collection<? extends GrantedAuthority> authority = Collections.singletonList(
      new SimpleGrantedAuthority("ROLE_USER"));
  private final RefreshTokenRedisRepository refreshTokenRedisRepository = mock(
      RefreshTokenRedisRepository.class);
  CustomUserDetails customUserDetailsObject = new CustomUserDetails(1L, "test@test.com",
      OAuth2Provider.GOOGLE, Role.USER, authority);
  private JwtTokenProvider jwtTokenProvider;
  private SecretKey secretKey;
  private Authentication authentication;

  @BeforeEach
  void init() {
    this.jwtTokenProvider = new JwtTokenProvider(SECRET, "cookie",
        this.refreshTokenRedisRepository);
    this.authentication = mock(Authentication.class);
    this.secretKey = Keys.hmacShaKeyFor(SECRET.getBytes(StandardCharsets.UTF_8));
  }

  @Test
  @DisplayName("accessToken 생성")
  void createAccessToken() {
    given(authentication.getPrincipal()).willReturn(customUserDetailsObject);

    String token = jwtTokenProvider.createAccessToken(authentication);
    Claims claims = Jwts.parserBuilder().setSigningKey(secretKey).build()
        .parseClaimsJws(token)
        .getBody();

    assertThat(token).isNotEmpty();
    assertEquals(Long.toString(customUserDetailsObject.getId()), claims.getSubject());
    assertEquals("dnd", claims.getIssuer());
  }

  @Test
  @DisplayName("refreshToken 생성")
  void createRefreshToken() {
    String refreshToken1 = jwtTokenProvider.createRefreshToken();
    Claims claims = Jwts.parserBuilder().setSigningKey(secretKey).build()
        .parseClaimsJws(refreshToken1)
        .getBody();

    assertThat(refreshToken1).isNotEmpty();
    assertEquals("dnd", claims.getIssuer());
  }

  @Test
  @DisplayName("응답에 refreshToken 추가")
  void addRefreshTokenByResponse() {
    MockHttpServletResponse response = mock(MockHttpServletResponse.class);

    String refreshToken2 = jwtTokenProvider.createRefreshToken();

    given(authentication.getPrincipal()).willReturn(customUserDetailsObject);
    given(refreshTokenRedisRepository.save(new RefreshToken())).willReturn(new RefreshToken());

    ResponseCookie cookie = ResponseCookie.from("cookie", refreshToken2)
        .httpOnly(true)
        .secure(true)
        .sameSite("dnd")
        .maxAge(1000L * 60 * 60 * 24 * 7 / 1000)
        .path("/")
        .build();

    jwtTokenProvider.addRefreshToken(authentication, response);

    verify(response).addHeader("Set-Cookie", cookie.toString());
  }
}
