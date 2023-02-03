package com.dnd.wedding.domain.jwt.controller;

import com.dnd.wedding.domain.jwt.service.JwtService;
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
  public ResponseEntity refreshToken(HttpServletRequest request, HttpServletResponse response,
      @Param("accessToken") String accessToken) {
    return ResponseEntity.ok().body(jwtService.refreshToken(request, response, accessToken));
  }
}
