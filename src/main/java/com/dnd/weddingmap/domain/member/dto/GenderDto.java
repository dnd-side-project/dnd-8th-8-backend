package com.dnd.weddingmap.domain.member.dto;

import com.dnd.weddingmap.domain.member.Gender;
import com.dnd.weddingmap.global.validator.ValidEnum;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GenderDto {

  @ValidEnum(value = Gender.class, message = "성별은 남자, 여자 중 하나여야 합니다.")
  private Gender gender;

  @Builder
  public GenderDto(Gender gender) {
    this.gender = gender;
  }
}
