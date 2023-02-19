package com.dnd.weddingmap.domain.checklist.checklistsubitem.service;

import com.dnd.weddingmap.domain.checklist.checklistitem.ChecklistItem;
import com.dnd.weddingmap.domain.checklist.checklistsubitem.ChecklistSubItem;
import com.dnd.weddingmap.domain.checklist.checklistsubitem.dto.ChecklistSubItemDto;
import com.dnd.weddingmap.domain.checklist.checklistsubitem.dto.ChecklistSubItemStateDto;
import java.util.List;

public interface ChecklistSubItemService {

  List<ChecklistSubItemDto> findChecklistSubItems(Long checklistItemId);

  ChecklistSubItem saveChecklistSubItem(ChecklistSubItemDto checklistSubItemDto,
      ChecklistItem checklistItem);

  boolean withdrawChecklistSubItem(Long subItemId, Long itemId);

  ChecklistSubItem modifyChecklistSubItem(Long subItemId, Long itemId,
      ChecklistSubItemStateDto checklistSubItemStateDto);
}
