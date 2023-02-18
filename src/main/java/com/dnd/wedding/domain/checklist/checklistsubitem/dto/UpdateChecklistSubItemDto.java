package com.dnd.wedding.domain.checklist.checklistsubitem.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UpdateChecklistSubItemDto {

  @NotNull
  private Boolean isChecked;

  @Builder
  public UpdateChecklistSubItemDto(Boolean isChecked) {
    this.isChecked = isChecked;
  }
}