package com.dnd.wedding.domain.jwt.controller;

import com.dnd.wedding.domain.jwt.dto.AccessTokenResponse;
import com.dnd.wedding.domain.jwt.service.JwtService;
import com.dnd.wedding.global.exception.UnauthorizedException;
import com.dnd.wedding.global.response.SuccessResponse;
import com.dnd.wedding.global.util.HeaderUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/jwt")
public class JwtController {

  private final JwtService jwtService;

  @PostMapping("/refresh")
  public ResponseEntity<SuccessResponse> refreshToken(HttpServletRequest request,
      HttpServletResponse response) {

    String accessToken = HeaderUtil.getAccessToken(request);
    String newToken = jwtService.refreshToken(request, response, accessToken);

    if (newToken == null) {
      throw new UnauthorizedException("Failed renew access token");
    }

    return ResponseEntity.ok().body(
        SuccessResponse.builder().data(new AccessTokenResponse(newToken)).build()
    );
  }
}
