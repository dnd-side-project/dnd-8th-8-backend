package com.dnd.wedding.domain.jwt;

import com.dnd.wedding.domain.oauth.CustomUserDetails;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletResponse;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;
import javax.crypto.SecretKey;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

@Log4j2
@Component
public class JwtTokenProvider {

  private final String COOKIE_REFRESH_TOKEN_KEY;
  private final static Long ACCESS_TOKEN_EXPIRE_LENGTH = 1000L * 60 * 60;    // 1hour
  private final static Long REFRESH_TOKEN_EXPIRE_LENGTH = 1000L * 60 * 60 * 24 * 7;  // 1week
  private final static String AUTHORITIES_KEY = "role";
  private final SecretKey secretKey;

  public JwtTokenProvider(@Value("${app.auth.token.secret-key}") String secret,
      @Value("${app.auth.token.refresh-cookie-key}") String cookieKey) {
    this.secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    this.COOKIE_REFRESH_TOKEN_KEY = cookieKey;
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

  public void createRefreshToken(Authentication authentication, HttpServletResponse response) {
    Date now = new Date();
    Date validity = new Date(now.getTime() + REFRESH_TOKEN_EXPIRE_LENGTH);

    String refreshToken = Jwts.builder()
        .signWith(secretKey)
        .setIssuer("dnd")
        .setIssuedAt(now)
        .setExpiration(validity)
        .compact();

    saveRefreshToken(authentication, refreshToken);

    ResponseCookie cookie = ResponseCookie.from(COOKIE_REFRESH_TOKEN_KEY, refreshToken)
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

    // todo: redis
  }

  public Authentication getAuthentication(String accessToken) {
    Claims claims = parseClaims(accessToken);

    Collection<? extends GrantedAuthority> authorities =
        Arrays.stream(claims.get(AUTHORITIES_KEY).toString().split(","))
            .map(SimpleGrantedAuthority::new).collect(Collectors.toList());

    CustomUserDetails principal = new CustomUserDetails(Long.valueOf(claims.getSubject()), "",
        null, null, authorities);

    return new UsernamePasswordAuthenticationToken(principal, "", authorities);
  }

  public Boolean validateToken(String token) {
    try {
      Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token);
      return true;
    } catch (ExpiredJwtException e) {
      log.info("Expired JWT token.");
    } catch (UnsupportedJwtException e) {
      log.info("Unsupported JWT token.");
    } catch (IllegalArgumentException e) {
      log.info("JWT token compact of handler are invalid.");
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
