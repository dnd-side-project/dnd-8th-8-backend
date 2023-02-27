package com.dnd.weddingmap.domain.wedding.dto;

import java.time.LocalDate;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WeddingDayDto {

  LocalDate weddingDay;

  @Builder
  public WeddingDayDto(LocalDate weddingDay) {
    this.weddingDay = weddingDay;
  }
}
