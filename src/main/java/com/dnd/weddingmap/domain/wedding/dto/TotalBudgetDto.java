package com.dnd.weddingmap.domain.wedding.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class TotalBudgetDto {

  Long totalBudget;

  @Builder
  public TotalBudgetDto(Long totalBudget) {
    this.totalBudget = totalBudget;
  }
}
