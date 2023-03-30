package com.dnd.weddingmap.domain.jwt;

import com.dnd.weddingmap.global.util.HeaderUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Log4j2
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

  private final JwtTokenProvider tokenProvider;

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
      FilterChain filterChain) throws ServletException, IOException {

    String accessToken = HeaderUtil.getAccessToken(request);

    if (accessToken != null && Boolean.TRUE.equals(tokenProvider.validateToken(accessToken))) {
      Authentication authentication = tokenProvider.getAuthentication(accessToken);
      SecurityContextHolder.getContext().setAuthentication(authentication);
      log.debug("save: " + authentication.getName() + "credentials");
    } else {
      log.debug("no valid JWT tokens.");
    }

    filterChain.doFilter(request, response);
  }
}
