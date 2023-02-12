package com.dnd.wedding.domain.checklist.checklistitem.service;

import com.dnd.wedding.domain.checklist.checklistitem.ChecklistItem;
import com.dnd.wedding.domain.checklist.checklistitem.dto.ChecklistItemDto;
import com.dnd.wedding.domain.checklist.checklistitem.dto.ChecklistItemResponseDto;
import com.dnd.wedding.domain.checklist.checklistitem.dto.CreateChecklistItemDto;
import com.dnd.wedding.domain.checklist.checklistsubitem.ChecklistSubItem;
import com.dnd.wedding.domain.checklist.checklistsubitem.dto.ChecklistSubItemDto;
import com.dnd.wedding.domain.member.Member;
import java.util.List;
import java.util.Optional;


public interface ChecklistItemService {

  Optional<ChecklistItem> findChecklistItemById(Long id);

  ChecklistItemResponseDto createChecklistItem(CreateChecklistItemDto dto, Member member);

  ChecklistItem saveChecklistItem(ChecklistItemDto checklistItemDto, Member member);

  List<ChecklistSubItem> saveChecklistSubItems(
      List<ChecklistSubItemDto> checklistSubItemDtoList,
      ChecklistItem checklistItem);
}
