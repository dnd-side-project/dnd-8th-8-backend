package com.dnd.weddingmap.domain.checklist.service.impl;

import com.dnd.weddingmap.domain.checklist.checklistitem.ChecklistItem;
import com.dnd.weddingmap.domain.checklist.checklistitem.dto.ChecklistItemApiDto;
import com.dnd.weddingmap.domain.checklist.checklistitem.dto.ChecklistItemDto;
import com.dnd.weddingmap.domain.checklist.checklistitem.repository.ChecklistItemRepository;
import com.dnd.weddingmap.domain.checklist.checklistsubitem.ChecklistSubItem;
import com.dnd.weddingmap.domain.checklist.checklistsubitem.dto.ChecklistSubItemDto;
import com.dnd.weddingmap.domain.checklist.checklistsubitem.repository.ChecklistSubItemRepository;
import com.dnd.weddingmap.domain.checklist.dto.PreChecklistItemListDto;
import com.dnd.weddingmap.domain.checklist.service.ChecklistService;
import com.dnd.weddingmap.domain.member.Member;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ChecklistServiceImpl implements ChecklistService {

  private final ChecklistItemRepository checklistItemRepository;
  private final ChecklistSubItemRepository checklistSubItemRepository;

  @Override
  @Transactional(readOnly = true)
  public List<ChecklistItemApiDto> findChecklistWithSubitem(Long memberId) {
    List<ChecklistItem> checklistItemList = checklistItemRepository.findByMemberIdOrderByCheckDate(
        memberId);
    List<ChecklistItemApiDto> result = new ArrayList<>();
    checklistItemList.forEach(item -> result.add(toDto(item, findChecklistSubItem(item.getId()))));

    return result;
  }

  @Override
  @Transactional(readOnly = true)
  public List<ChecklistItemDto> findChecklist(Long memberId) {
    List<ChecklistItem> checklistItemList = checklistItemRepository.findByMemberIdOrderByCheckDate(
        memberId);
    List<ChecklistItemDto> result = new ArrayList<>();
    checklistItemList.forEach(item -> result.add(new ChecklistItemDto(item)));

    return result;
  }

  @Override
  @Transactional
  public List<ChecklistItemDto> savePreChecklistItemList(Member member,
      PreChecklistItemListDto preChecklistItemListDto) {
    return preChecklistItemListDto.getPreChecklistItems().stream()
        .map(preChecklistItem ->
            new ChecklistItemDto(checklistItemRepository.save(ChecklistItem.builder()
                .title(preChecklistItem.getTitle())
                .member(member)
                .build()))).toList();
  }

  @Transactional(readOnly = true)
  public List<ChecklistSubItem> findChecklistSubItem(Long checklistItemId) {
    return checklistSubItemRepository.findAllByChecklistItemId(checklistItemId);
  }

  private ChecklistItemApiDto toDto(ChecklistItem checklistItem,
      List<ChecklistSubItem> checklistSubItems) {
    List<ChecklistSubItemDto> checklistSubItemDtoList = checklistSubItems.stream()
        .map(ChecklistSubItemDto::new).toList();

    return new ChecklistItemApiDto(new ChecklistItemDto(checklistItem), checklistSubItemDtoList);
  }
}
