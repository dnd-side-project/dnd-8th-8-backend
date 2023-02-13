package com.dnd.wedding.domain.checklist.checklistsubitem.service;

import com.dnd.wedding.domain.checklist.checklistitem.ChecklistItem;
import com.dnd.wedding.domain.checklist.checklistsubitem.ChecklistSubItem;
import com.dnd.wedding.domain.checklist.checklistsubitem.dto.ChecklistSubItemDto;
import com.dnd.wedding.domain.checklist.checklistsubitem.dto.UpdateChecklistSubItemDto;
import java.util.List;

public interface ChecklistSubItemService {

  List<ChecklistSubItem> findChecklistSubItems(Long checklistItemId);

  ChecklistSubItem saveChecklistSubItem(ChecklistSubItemDto checklistSubItemDto,
      ChecklistItem checklistItem);

  boolean withdrawChecklistSubItem(Long subItemId, Long itemId);

  ChecklistSubItem modifyChecklistSubItem(Long subItemId, Long itemId,
      UpdateChecklistSubItemDto checklistSubItemDto);
}
