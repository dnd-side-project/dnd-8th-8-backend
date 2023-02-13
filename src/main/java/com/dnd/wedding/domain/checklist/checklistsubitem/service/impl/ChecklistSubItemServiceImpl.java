package com.dnd.wedding.domain.checklist.checklistsubitem.service.impl;

import com.dnd.wedding.domain.checklist.checklistitem.ChecklistItem;
import com.dnd.wedding.domain.checklist.checklistsubitem.ChecklistSubItem;
import com.dnd.wedding.domain.checklist.checklistsubitem.dto.ChecklistSubItemDto;
import com.dnd.wedding.domain.checklist.checklistsubitem.dto.UpdateChecklistSubItemDto;
import com.dnd.wedding.domain.checklist.checklistsubitem.repository.ChecklistSubItemRepository;
import com.dnd.wedding.domain.checklist.checklistsubitem.service.ChecklistSubItemService;
import com.dnd.wedding.global.exception.NotFoundException;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Objects;
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

  @Transactional
  @Override
  public boolean withdrawChecklistSubItem(Long subItemId, Long itemId) {
    ChecklistSubItem checklistSubItem = checklistSubItemRepository.findById(subItemId)
        .orElseThrow(() -> new NotFoundException("존재하지 않는 체크리스트 서브 아이템입니다."));

    if (!Objects.equals(checklistSubItem.getChecklistItem().getId(), itemId)) {
      return false;
    }
    checklistSubItemRepository.delete(checklistSubItem);
    return true;
  }

  @Transactional
  @Override
  public ChecklistSubItem modifyChecklistSubItem(Long subItemId, Long itemId,
      UpdateChecklistSubItemDto checklistSubItemDto) {
    ChecklistSubItem checklistSubItem = checklistSubItemRepository.findById(subItemId)
        .orElseThrow(() -> new NotFoundException("존재하지 않는 체크리스트 서브 아이템입니다."));

    if (!Objects.equals(checklistSubItem.getChecklistItem().getId(), itemId)) {
      return null;
    }
    return checklistSubItem.updateState(checklistSubItemDto);
  }
}
