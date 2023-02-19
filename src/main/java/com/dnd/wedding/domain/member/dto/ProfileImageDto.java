package com.dnd.wedding.domain.member.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProfileImageDto {
  private String url;

  @Builder
  public ProfileImageDto(String url) {
    this.url = url;
  }
}
