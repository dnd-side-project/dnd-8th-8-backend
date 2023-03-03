package com.dnd.weddingmap.domain.checklist.checklistsubitem.service.impl;

import com.dnd.weddingmap.domain.checklist.checklistitem.ChecklistItem;
import com.dnd.weddingmap.domain.checklist.checklistsubitem.ChecklistSubItem;
import com.dnd.weddingmap.domain.checklist.checklistsubitem.dto.ChecklistSubItemDto;
import com.dnd.weddingmap.domain.checklist.checklistsubitem.dto.ChecklistSubItemStateDto;
import com.dnd.weddingmap.domain.checklist.checklistsubitem.repository.ChecklistSubItemRepository;
import com.dnd.weddingmap.domain.checklist.checklistsubitem.service.ChecklistSubItemService;
import com.dnd.weddingmap.global.exception.NotFoundException;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ChecklistSubItemServiceImpl implements ChecklistSubItemService {

  private final ChecklistSubItemRepository checklistSubItemRepository;

  @Transactional
  @Override
  public List<ChecklistSubItemDto> findChecklistSubItems(Long checklistItemId) {
    List<ChecklistSubItem> checklistSubItems = checklistSubItemRepository.findAllByChecklistItemId(
        checklistItemId);
    return checklistSubItems.stream().map(ChecklistSubItemDto::new).toList();
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
      ChecklistSubItemStateDto checklistSubItemStateDto) {
    ChecklistSubItem checklistSubItem = checklistSubItemRepository.findById(subItemId)
        .orElseThrow(() -> new NotFoundException("존재하지 않는 체크리스트 서브 아이템입니다."));

    if (!Objects.equals(checklistSubItem.getChecklistItem().getId(), itemId)) {
      return null;
    }
    return checklistSubItem.updateState(checklistSubItemStateDto);
  }
}
