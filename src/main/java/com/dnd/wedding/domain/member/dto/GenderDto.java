package com.dnd.wedding.domain.member.dto;

import com.dnd.wedding.domain.member.Gender;
import lombok.Builder;
import lombok.Getter;

@Getter
public class GenderDto {

  private Gender gender;

  @Builder
  public GenderDto(Gender gender) {
    this.gender = gender;
  }
}
