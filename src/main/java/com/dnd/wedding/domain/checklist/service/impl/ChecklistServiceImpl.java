package com.dnd.wedding.domain.checklist.service.impl;

import com.dnd.wedding.domain.checklist.checklistitem.ChecklistItem;
import com.dnd.wedding.domain.checklist.checklistitem.dto.ChecklistItemApiDto;
import com.dnd.wedding.domain.checklist.checklistitem.dto.ChecklistItemDto;
import com.dnd.wedding.domain.checklist.checklistitem.repository.ChecklistItemRepository;
import com.dnd.wedding.domain.checklist.checklistsubitem.ChecklistSubItem;
import com.dnd.wedding.domain.checklist.checklistsubitem.dto.ChecklistSubItemDto;
import com.dnd.wedding.domain.checklist.checklistsubitem.repository.ChecklistSubItemRepository;
import com.dnd.wedding.domain.checklist.service.ChecklistService;
import jakarta.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ChecklistServiceImpl implements ChecklistService {

  private final ChecklistItemRepository checklistItemRepository;
  private final ChecklistSubItemRepository checklistSubItemRepository;

  @Transactional
  @Override
  public List<ChecklistItemApiDto> findChecklist(Long memberId) {
    List<ChecklistItem> checklistItemList = checklistItemRepository.findByMemberId(
        memberId);
    List<ChecklistItemApiDto> result = new ArrayList<>();
    checklistItemList.forEach(item -> result.add(toDto(item, findChecklistSubItem(item.getId()))));

    return result;
  }

  public List<ChecklistSubItem> findChecklistSubItem(Long checklistItemId) {
    return checklistSubItemRepository.findAllByChecklistItem_Id(checklistItemId);
  }

  private ChecklistItemApiDto toDto(ChecklistItem checklistItem,
      List<ChecklistSubItem> checklistSubItems) {
    List<ChecklistSubItemDto> checklistSubItemDtoList = checklistSubItems.stream()
        .map(ChecklistSubItemDto::new).toList();

    return new ChecklistItemApiDto(new ChecklistItemDto(checklistItem), checklistSubItemDtoList);
  }
}
