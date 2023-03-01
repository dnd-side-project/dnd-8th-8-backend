package com.dnd.weddingmap.domain.checklist.checklistitem.service;

import com.dnd.weddingmap.domain.checklist.checklistitem.ChecklistItem;
import com.dnd.weddingmap.domain.checklist.checklistitem.dto.ChecklistItemApiDto;
import com.dnd.weddingmap.domain.checklist.checklistitem.dto.ChecklistItemDto;
import com.dnd.weddingmap.domain.checklist.checklistsubitem.ChecklistSubItem;
import com.dnd.weddingmap.domain.checklist.checklistsubitem.dto.ChecklistSubItemDto;
import com.dnd.weddingmap.domain.member.Member;
import java.util.List;

public interface ChecklistItemService {

  ChecklistItemApiDto createChecklistItem(ChecklistItemApiDto dto, Member member);

  ChecklistItem saveChecklistItem(ChecklistItemDto checklistItemDto, Member member);

  List<ChecklistSubItem> saveChecklistSubItemList(
      List<ChecklistSubItemDto> checklistSubItemDtoList,
      ChecklistItem checklistItem);

  ChecklistItemApiDto modifyChecklistItem(Long checklistItemId, ChecklistItemApiDto dto);

  ChecklistItem updateChecklistItem(Long checklistItemId, ChecklistItemDto checklistItemDto);


  List<ChecklistSubItem> updateChecklistSubItemList(
      List<ChecklistSubItemDto> checklistSubItemDtoList);

  ChecklistSubItem updateChecklistSubItem(ChecklistSubItemDto checklistSubItemDto);

  boolean withdrawChecklistItem(Long id);

  ChecklistItem checkPermission(Long checklistItemId, Long memberId);
}
