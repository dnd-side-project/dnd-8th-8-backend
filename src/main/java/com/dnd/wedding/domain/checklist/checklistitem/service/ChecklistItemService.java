package com.dnd.wedding.domain.checklist.checklistitem.service;

import com.dnd.wedding.domain.checklist.checklistitem.ChecklistItem;
import java.util.Optional;


public interface ChecklistItemService {

  Optional<ChecklistItem> findChecklistItemById(Long id);
}
