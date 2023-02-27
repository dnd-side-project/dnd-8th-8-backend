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

  @Builder
  public WeddingDayDto(LocalDate weddingDay) {
    this.weddingDay = weddingDay;
  }
}
