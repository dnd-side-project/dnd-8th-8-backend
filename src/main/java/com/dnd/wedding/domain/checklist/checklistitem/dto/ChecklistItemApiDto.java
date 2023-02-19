package com.dnd.wedding.domain.checklist.checklistitem.dto;

import com.dnd.wedding.domain.checklist.checklistsubitem.dto.ChecklistSubItemDto;
import jakarta.validation.Valid;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ChecklistItemApiDto {

  @Valid
  private ChecklistItemDto checklistItem;
  @Valid
  private List<ChecklistSubItemDto> checklistSubItems;

  @Builder
  public ChecklistItemApiDto(ChecklistItemDto checklistItem,
      List<ChecklistSubItemDto> checklistSubItems) {
    this.checklistItem = checklistItem;
    this.checklistSubItems = checklistSubItems;

  }
}
