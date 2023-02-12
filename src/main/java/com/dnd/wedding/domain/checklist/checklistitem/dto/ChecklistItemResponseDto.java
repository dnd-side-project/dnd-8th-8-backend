package com.dnd.wedding.domain.checklist.checklistitem.dto;

import com.dnd.wedding.domain.checklist.checklistsubitem.dto.ChecklistSubItemDto;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ChecklistItemResponseDto {

  private ChecklistItemDto checklistItem;
  private List<ChecklistSubItemDto> checklistSubItems;
}
