package com.dnd.wedding.domain.checklist.checklistitem.dto;

import com.dnd.wedding.domain.checklist.checklistsubitem.dto.ChecklistSubItemDto;
import jakarta.validation.Valid;
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

  @Valid
  private ChecklistItemDto checklistItem;
  @Valid
  private List<ChecklistSubItemDto> checklistSubItems;
}
