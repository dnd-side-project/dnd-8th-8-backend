package com.dnd.weddingmap.domain.wedding.dto;

import java.time.LocalDate;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class WeddingDayDto {

  LocalDate weddingDay;
  boolean preparing;

  @Builder
  public WeddingDayDto(LocalDate weddingDay, boolean preparing) {
    this.weddingDay = weddingDay;
    this.preparing = preparing;
  }
}
