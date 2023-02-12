package com.dnd.wedding.domain.checklist.checklistsubitem.service.impl;

import com.dnd.wedding.domain.checklist.checklistitem.ChecklistItem;
import com.dnd.wedding.domain.checklist.checklistsubitem.ChecklistSubItem;
import com.dnd.wedding.domain.checklist.checklistsubitem.dto.ChecklistSubItemDto;
import com.dnd.wedding.domain.checklist.checklistsubitem.repository.ChecklistSubItemRepository;
import com.dnd.wedding.domain.checklist.checklistsubitem.service.ChecklistSubItemService;
import jakarta.transaction.Transactional;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChecklistSubItemServiceImpl implements ChecklistSubItemService {

  private final ChecklistSubItemRepository checklistSubItemRepository;

  @Override
  public List<ChecklistSubItem> findChecklistSubItems(Long checklistItemId) {
    return checklistSubItemRepository.findAllByChecklistItem_Id(checklistItemId);
  }

  @Transactional
  @Override
  public ChecklistSubItem saveChecklistSubItem(ChecklistSubItemDto checklistSubItemDto,
      ChecklistItem checklistItem) {
    return checklistSubItemRepository.save(checklistSubItemDto.toEntity(checklistItem));
  }
}
