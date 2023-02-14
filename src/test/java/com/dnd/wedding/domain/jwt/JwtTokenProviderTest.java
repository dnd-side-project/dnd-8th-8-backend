package com.dnd.wedding.domain.jwt;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import com.dnd.wedding.domain.jwt.repository.RefreshTokenRedisRepository;
import com.dnd.wedding.domain.member.Member;
import com.dnd.wedding.domain.member.Role;
import com.dnd.wedding.domain.oauth.CustomUserDetails;
import com.dnd.wedding.domain.oauth.OAuth2Provider;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import javax.crypto.SecretKey;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseCookie;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.Authentication;

class JwtTokenProviderTest {

  private static final String SECRET =
      "testSecretKeytestSecretKeytestSecretKeytestSecretKeytestSecretKeytestSecretKeytestSecretKey";

  private final RefreshTokenRedisRepository refreshTokenRedisRepository = mock(
      RefreshTokenRedisRepository.class);
  private final CustomUserDetails customUserDetailsObject =
      new CustomUserDetails(
          Member.builder()
              .id(1L)
              .email("test@test.com")
              .name("test")
              .role(Role.USER)
              .oauth2Provider(OAuth2Provider.GOOGLE)
              .build());

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

    ResponseCookie cookie1 = ResponseCookie.from("cookie", refreshToken2)
        .httpOnly(true)
        .secure(true)
        .sameSite("dnd")
        .maxAge(1000L * 60 * 60 * 24 * 7 / 1000)
        .path("/")
        .build();

    jwtTokenProvider.addRefreshToken(authentication, response);

    verify(response).addHeader("Set-Cookie", cookie1.toString());
  }

  @Test
  @DisplayName("accessToken 인증 객체 조회")
  void getAuthenticationByToken() {
    Date now = new Date();
    Date validity = new Date(now.getTime() + 1000L * 60 * 60);

    String token = Jwts.builder()
        .signWith(secretKey)
        .setSubject("1")
        .claim("role", "ROLE_USER")
        .setIssuer("dnd")
        .setIssuedAt(now)
        .setExpiration(validity)
        .compact();

    Authentication authentication1 = jwtTokenProvider.getAuthentication(token);
    CustomUserDetails principal = (CustomUserDetails) authentication1.getPrincipal();

    assertTrue(authentication1.isAuthenticated());
    assertEquals(1, principal.getId());
  }

  @Test
  @DisplayName("만료된 accessToken 인증 객체 조회")
  void getAuthenticationByExpiredToken() {
    Date now = new Date();
    Date validity = new Date(now.getTime());

    String token = Jwts.builder()
        .signWith(secretKey)
        .setSubject("1")
        .claim("role", "ROLE_USER")
        .setIssuer("dnd")
        .setIssuedAt(now)
        .setExpiration(validity)
        .compact();

    Authentication authentication2 = jwtTokenProvider.getAuthentication(token);
    CustomUserDetails principal = (CustomUserDetails) authentication2.getPrincipal();
    assertEquals(1, principal.getId());
  }

  @Test
  @DisplayName("token 검사 성공")
  void validateToken() {
    Date now = new Date();
    Date validity = new Date(now.getTime() + 1000L * 60 * 60);

    String token = Jwts.builder()
        .signWith(secretKey)
        .setSubject("1")
        .claim("role", "ROLE_USER")
        .setIssuer("dnd")
        .setIssuedAt(now)
        .setExpiration(validity)
        .compact();

    assertTrue(jwtTokenProvider.validateToken(token));
  }

  @Test
  @DisplayName("만료된 토큰일 경우 false를 리턴한다.")
  void validateByExpiredToken() {
    Date now = new Date();
    Date validity = new Date(now.getTime());

    String token = Jwts.builder()
        .signWith(secretKey)
        .setSubject("1")
        .claim("role", "ROLE_USER")
        .setIssuer("dnd")
        .setIssuedAt(now)
        .setExpiration(validity)
        .compact();

    assertFalse(jwtTokenProvider.validateToken(token));
  }

  @Test
  @DisplayName("JWT 토큰 양식과 일치하지 않는 경우 false를 리턴한다.")
  void validateByUnsupportedToken() {
    assertFalse(jwtTokenProvider
        .validateToken(
            "eyJhbGciOiJIUzI1NiJ9.NTY3ODkwRG9lIE2MjM5MDIyfQ."));
  }

  @Test
  @DisplayName("올바르지 않은 JWT 구성일 경우 false를 리턴한다.")
  void validateByMalformedToken() {
    assertFalse(jwtTokenProvider.validateToken("test.token."));
  }
}
