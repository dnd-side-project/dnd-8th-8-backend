package com.dnd.wedding.domain.checklist.checklistsubitem.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UpdateChecklistSubItemDto {

  @NotEmpty
  private Boolean isChecked;
}
