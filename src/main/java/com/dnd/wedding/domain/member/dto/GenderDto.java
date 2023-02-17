package com.dnd.wedding.domain.member.dto;

import com.dnd.wedding.domain.member.Gender;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GenderDto {

  private Gender gender;

  @Builder
  public GenderDto(Gender gender) {
    this.gender = gender;
  }
}
