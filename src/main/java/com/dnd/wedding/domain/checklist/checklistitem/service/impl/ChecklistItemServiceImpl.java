package com.dnd.wedding.domain.checklist.checklistitem.service.impl;

import com.dnd.wedding.domain.checklist.checklistitem.ChecklistItem;
import com.dnd.wedding.domain.checklist.checklistitem.dto.ChecklistItemDto;
import com.dnd.wedding.domain.checklist.checklistitem.dto.ChecklistItemResponseDto;
import com.dnd.wedding.domain.checklist.checklistitem.dto.CreateChecklistItemDto;
import com.dnd.wedding.domain.checklist.checklistitem.repository.ChecklistItemRepository;
import com.dnd.wedding.domain.checklist.checklistitem.service.ChecklistItemService;
import com.dnd.wedding.domain.checklist.checklistsubitem.ChecklistSubItem;
import com.dnd.wedding.domain.checklist.checklistsubitem.dto.ChecklistSubItemDto;
import com.dnd.wedding.domain.checklist.checklistsubitem.repository.ChecklistSubItemRepository;
import com.dnd.wedding.domain.member.Member;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ChecklistItemServiceImpl implements ChecklistItemService {

  private final ChecklistItemRepository checklistItemRepository;
  private final ChecklistSubItemRepository checklistSubItemRepository;

  @Override
  public Optional<ChecklistItem> findChecklistItemById(Long id) {
    return checklistItemRepository.findById(id);
  }

  @Transactional
  @Override
  public ChecklistItemResponseDto createChecklistItem(CreateChecklistItemDto dto, Member member) {
    ChecklistItem savedChecklistItem = saveChecklistItem(dto.getChecklistItem(), member);
    List<ChecklistSubItem> savedChecklistSubItems = saveChecklistSubItems(
        dto.getChecklistSubItems(), savedChecklistItem);

    List<ChecklistSubItemDto> checklistSubItemDtoList = savedChecklistSubItems.stream()
        .map(ChecklistSubItemDto::new).toList();

    return new ChecklistItemResponseDto(
        new ChecklistItemDto(savedChecklistItem), checklistSubItemDtoList);
  }

  @Transactional
  @Override
  public ChecklistItem saveChecklistItem(ChecklistItemDto checklistItemDto, Member member) {
    return checklistItemRepository.save(checklistItemDto.toEntity(member));
  }

  @Transactional
  @Override
  public List<ChecklistSubItem> saveChecklistSubItems(
      List<ChecklistSubItemDto> checklistSubItemDtoList,
      ChecklistItem checklistItem) {
    List<ChecklistSubItem> checklistSubItems = checklistSubItemDtoList.stream()
        .map(subItemDto -> subItemDto.toEntity(checklistItem)).toList();
    return checklistSubItemRepository.saveAll(checklistSubItems);
  }
}
