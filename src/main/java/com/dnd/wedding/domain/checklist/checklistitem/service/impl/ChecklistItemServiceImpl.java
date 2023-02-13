package com.dnd.wedding.domain.checklist.checklistitem.service.impl;

import com.dnd.wedding.domain.checklist.checklistitem.ChecklistItem;
import com.dnd.wedding.domain.checklist.checklistitem.dto.ChecklistItemApiDto;
import com.dnd.wedding.domain.checklist.checklistitem.dto.ChecklistItemDto;
import com.dnd.wedding.domain.checklist.checklistitem.repository.ChecklistItemRepository;
import com.dnd.wedding.domain.checklist.checklistitem.service.ChecklistItemService;
import com.dnd.wedding.domain.checklist.checklistsubitem.ChecklistSubItem;
import com.dnd.wedding.domain.checklist.checklistsubitem.dto.ChecklistSubItemDto;
import com.dnd.wedding.domain.checklist.checklistsubitem.repository.ChecklistSubItemRepository;
import com.dnd.wedding.domain.member.Member;
import com.dnd.wedding.global.exception.BadRequestException;
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
  public ChecklistItemApiDto createChecklistItem(ChecklistItemApiDto dto, Member member) {
    ChecklistItem savedChecklistItem = saveChecklistItem(dto.getChecklistItem(), member);
    List<ChecklistSubItem> savedChecklistSubItems = saveChecklistSubItems(
        dto.getChecklistSubItems(), savedChecklistItem);

    List<ChecklistSubItemDto> checklistSubItemDtoList = savedChecklistSubItems.stream()
        .map(ChecklistSubItemDto::new).toList();

    return new ChecklistItemApiDto(
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

  @Transactional
  @Override
  public ChecklistItemApiDto modifyChecklistItem(Long checklistItemId,
      ChecklistItemApiDto dto) {
    ChecklistItem modifiedChecklistItem = updateChecklistItem(
        checklistItemId, dto.getChecklistItem());
    List<ChecklistSubItem> modifiedChecklistSubItems = updateChecklistSubItems(
        dto.getChecklistSubItems());

    List<ChecklistSubItemDto> checklistSubItemDtoList = modifiedChecklistSubItems.stream()
        .map(ChecklistSubItemDto::new).toList();

    return new ChecklistItemApiDto(
        new ChecklistItemDto(modifiedChecklistItem), checklistSubItemDtoList);
  }

  @Transactional
  @Override
  public ChecklistItem updateChecklistItem(Long checklistItemId,
      ChecklistItemDto checklistItemDto) {
    Optional<ChecklistItem> checklistItem = checklistItemRepository.findOneById(
        checklistItemId);

    if (checklistItem.isEmpty()) {
      throw new BadRequestException("not existed checklist item");
    }
    return checklistItem.get().update(checklistItemDto);
  }

  @Transactional
  @Override
  public List<ChecklistSubItem> updateChecklistSubItems(
      List<ChecklistSubItemDto> checklistSubItemDtoList) {
    return checklistSubItemDtoList.stream().map(this::updateChecklistSubItem).toList();
  }

  @Transactional
  @Override
  public ChecklistSubItem updateChecklistSubItem(ChecklistSubItemDto checklistSubItemDto) {
    Optional<ChecklistSubItem> checklistSubItem = checklistSubItemRepository.findOneById(
        checklistSubItemDto.getId());

    if (checklistSubItem.isEmpty()) {
      throw new BadRequestException("not existed checklist sub-item");
    }
    return checklistSubItem.get().update(checklistSubItemDto);
  }

  @Transactional
  @Override
  public boolean withdrawChecklistItem(Long id) {
    return checklistItemRepository.findById(id)
        .map(checklistItem -> {
          checklistItemRepository.delete(checklistItem);
          return true;
        })
        .orElse(false);
  }
}
