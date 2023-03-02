package com.dnd.weddingmap.domain.member.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class NameDto {

  private String name;

  @Builder
  public NameDto(String name) {
    this.name = name;
  }
}
