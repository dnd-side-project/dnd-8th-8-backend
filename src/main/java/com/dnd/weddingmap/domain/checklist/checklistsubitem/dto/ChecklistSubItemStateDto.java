package com.dnd.weddingmap.domain.checklist.checklistsubitem.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ChecklistSubItemStateDto {

  @NotNull
  private Boolean isChecked;

  @Builder
  public ChecklistSubItemStateDto(Boolean isChecked) {
    this.isChecked = isChecked;
  }
}
