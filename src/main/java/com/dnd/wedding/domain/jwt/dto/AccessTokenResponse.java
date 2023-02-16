package com.dnd.wedding.domain.jwt.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class AccessTokenResponse {

  private String accessToken;

  @Builder
  public AccessTokenResponse(String accessToken) {
    this.accessToken = accessToken;
  }
}
