package com.dnd.wedding.domain.member.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class ProfileImageDto {
  private String url;

  @Builder
  public ProfileImageDto(String url) {
    this.url = url;
  }
}
