package com.dnd.wedding.domain.checklist.checklistitem.dto;

import com.dnd.wedding.domain.checklist.checklistsubitem.dto.ChecklistSubItemDto;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChecklistItemApiDto {

  private ChecklistItemDto checklistItem;
  private List<ChecklistSubItemDto> checklistSubItems;
}
