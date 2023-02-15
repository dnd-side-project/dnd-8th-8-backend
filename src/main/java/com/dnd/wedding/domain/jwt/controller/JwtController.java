package com.dnd.wedding.domain.jwt.controller;

import com.dnd.wedding.domain.jwt.service.JwtService;
import com.dnd.wedding.global.exception.UnauthorizedException;
import io.lettuce.core.dynamic.annotation.Param;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/jwt")
public class JwtController {

  private final JwtService jwtService;

  @GetMapping("/refresh")
  public ResponseEntity<String> refreshToken(HttpServletRequest request,
      HttpServletResponse response, @Param("accessToken") String accessToken) {

    String newToken = jwtService.refreshToken(request, response, accessToken);

    if (newToken == null) {
      throw new UnauthorizedException("Failed renew access token");
    }

    return ResponseEntity.ok().body(newToken);
  }
}
