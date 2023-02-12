package com.dnd.wedding.domain.checklist.checklistsubitem.service;

import com.dnd.wedding.domain.checklist.checklistsubitem.ChecklistSubItem;
import java.util.List;

public interface ChecklistSubItemService {

  List<ChecklistSubItem> findChecklistSubItems(Long checklistItemId);
}
