package com.dnd.weddingmap.domain.wedding.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class BudgetDto {

  Long budget;

  @Builder
  public BudgetDto(Long budget) {
    this.budget = budget;
  }
}
