package com.dnd.wedding.domain.checklist.checklistitem.service.impl;

import com.dnd.wedding.domain.checklist.checklistitem.ChecklistItem;
import com.dnd.wedding.domain.checklist.checklistitem.repository.ChecklistItemRepository;
import com.dnd.wedding.domain.checklist.checklistitem.service.ChecklistItemService;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ChecklistItemServiceImpl implements ChecklistItemService {

  private final ChecklistItemRepository checklistItemRepository;

  @Override
  public Optional<ChecklistItem> findChecklistItemById(Long id) {
    return checklistItemRepository.findById(id);
  }
}
