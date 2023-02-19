package com.dnd.weddingmap.domain.jwt;

import com.dnd.weddingmap.domain.jwt.repository.RefreshTokenRedisRepository;
import com.dnd.weddingmap.domain.oauth.CustomUserDetails;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletResponse;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.stream.Collectors;
import javax.crypto.SecretKey;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

@Log4j2
@Component
public class JwtTokenProvider {

  private final String cookieRefreshTokenKey;
  private static final Long ACCESS_TOKEN_EXPIRE_LENGTH = 1000L * 60 * 60;    // 1hour
  private static final Long REFRESH_TOKEN_EXPIRE_LENGTH = 1000L * 60 * 60 * 24 * 7;  // 1week
  private static final String AUTHORITIES_KEY = "role";
  private final SecretKey secretKey;
  private final RefreshTokenRedisRepository refreshTokenRedisRepository;

  private final UserDetailsService userDetailsService;

  public JwtTokenProvider(@Value("${app.auth.token.secret-key}") String secret,
      @Value("${app.auth.token.refresh-cookie-key}") String cookieKey,
      RefreshTokenRedisRepository refreshTokenRedisRepository,
      UserDetailsService userDetailsService) {
    this.refreshTokenRedisRepository = refreshTokenRedisRepository;
    this.secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    this.cookieRefreshTokenKey = cookieKey;
    this.userDetailsService = userDetailsService;
  }

  public String createAccessToken(Authentication authentication) {
    Date now = new Date();
    Date validity = new Date(now.getTime() + ACCESS_TOKEN_EXPIRE_LENGTH);

    CustomUserDetails user = (CustomUserDetails) authentication.getPrincipal();

    Long userId = user.getId();
    String role = authentication.getAuthorities().stream()
        .map(GrantedAuthority::getAuthority)
        .collect(Collectors.joining(","));

    return Jwts.builder()
        .signWith(secretKey)
        .setSubject(String.valueOf(userId))
        .claim(AUTHORITIES_KEY, role)
        .setIssuer("dnd")
        .setIssuedAt(now)
        .setExpiration(validity)
        .compact();
  }

  public String createRefreshToken() {
    Date now = new Date();
    Date validity = new Date(now.getTime() + REFRESH_TOKEN_EXPIRE_LENGTH);

    return Jwts.builder()
        .signWith(secretKey)
        .setIssuer("dnd")
        .setIssuedAt(now)
        .setExpiration(validity)
        .compact();
  }

  public void addRefreshToken(Authentication authentication, HttpServletResponse response) {
    String refreshToken = createRefreshToken();

    saveRefreshToken(authentication, refreshToken);

    ResponseCookie cookie = ResponseCookie.from(cookieRefreshTokenKey, refreshToken)
        .httpOnly(true)
        .secure(true)
        .sameSite("dnd")
        .maxAge(REFRESH_TOKEN_EXPIRE_LENGTH / 1000)
        .path("/")
        .build();

    response.addHeader("Set-Cookie", cookie.toString());
  }

  private void saveRefreshToken(Authentication authentication, String refreshToken) {
    CustomUserDetails user = (CustomUserDetails) authentication.getPrincipal();

    refreshTokenRedisRepository.save(RefreshToken.builder()
        .id(user.getId())
        .token(refreshToken)
        .expiredTime(REFRESH_TOKEN_EXPIRE_LENGTH)
        .build());
  }

  public Authentication getAuthentication(String accessToken) {
    Claims claims = parseClaims(accessToken);
    UserDetails userDetails = userDetailsService.loadUserByUsername(claims.getSubject());
    return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
  }

  public Boolean validateToken(String token) {
    try {
      Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token);
      return true;
    } catch (ExpiredJwtException e) {
      log.info("Expired JWT token.");
    } catch (UnsupportedJwtException e) {
      log.info("Unsupported JWT token.");
    } catch (MalformedJwtException e) {
      log.info("Malformed Jwt token.");
    } catch (Exception e) {
      log.info(e.getMessage());
    }
    return false;
  }

  private Claims parseClaims(String accessToken) {
    try {
      return Jwts.parserBuilder().setSigningKey(secretKey).build()
          .parseClaimsJws(accessToken)
          .getBody();
    } catch (ExpiredJwtException e) {
      return e.getClaims();
    }
  }
}
