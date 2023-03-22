package com.dnd.weddingmap.domain.checklist.checklistitem.service.impl;

import com.dnd.weddingmap.domain.checklist.checklistitem.ChecklistItem;
import com.dnd.weddingmap.domain.checklist.checklistitem.dto.ChecklistItemApiDto;
import com.dnd.weddingmap.domain.checklist.checklistitem.dto.ChecklistItemDto;
import com.dnd.weddingmap.domain.checklist.checklistitem.repository.ChecklistItemRepository;
import com.dnd.weddingmap.domain.checklist.checklistitem.service.ChecklistItemService;
import com.dnd.weddingmap.domain.checklist.checklistsubitem.ChecklistSubItem;
import com.dnd.weddingmap.domain.checklist.checklistsubitem.dto.ChecklistSubItemDto;
import com.dnd.weddingmap.domain.checklist.checklistsubitem.repository.ChecklistSubItemRepository;
import com.dnd.weddingmap.domain.member.Member;
import com.dnd.weddingmap.global.exception.BadRequestException;
import com.dnd.weddingmap.global.exception.ForbiddenException;
import com.dnd.weddingmap.global.exception.NotFoundException;
import com.dnd.weddingmap.global.util.MessageUtil;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ChecklistItemServiceImpl implements ChecklistItemService {

  private final ChecklistItemRepository checklistItemRepository;
  private final ChecklistSubItemRepository checklistSubItemRepository;

  @Override
  @Transactional(rollbackFor = Exception.class)
  public ChecklistItemApiDto createChecklistItem(ChecklistItemApiDto dto, Member member) {
    ChecklistItem savedChecklistItem = saveChecklistItem(dto.getChecklistItem(), member);

    if (dto.getChecklistSubItems() != null) {
      List<ChecklistSubItem> savedChecklistSubItems = saveChecklistSubItemList(
          dto.getChecklistSubItems(), savedChecklistItem);

      return ChecklistItemApiDto.builder()
          .checklistItem(new ChecklistItemDto(savedChecklistItem))
          .checklistSubItems(savedChecklistSubItems.stream()
              .map(ChecklistSubItemDto::new).toList())
          .build();
    }

    return ChecklistItemApiDto.builder()
        .checklistItem(new ChecklistItemDto(savedChecklistItem))
        .build();
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public ChecklistItem saveChecklistItem(ChecklistItemDto checklistItemDto, Member member) {
    return checklistItemRepository.save(checklistItemDto.toEntity(member));
  }

  @Transactional(rollbackFor = Exception.class)
  @Override
  public List<ChecklistSubItem> saveChecklistSubItemList(
      List<ChecklistSubItemDto> checklistSubItemDtoList,
      ChecklistItem checklistItem) {
    List<ChecklistSubItem> checklistSubItems = checklistSubItemDtoList.stream()
        .map(subItemDto -> subItemDto.toEntity(checklistItem)).toList();
    return checklistSubItemRepository.saveAll(checklistSubItems);
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public ChecklistItemApiDto modifyChecklistItem(Long checklistItemId,
      ChecklistItemApiDto dto) {
    ChecklistItem modifiedChecklistItem = updateChecklistItem(
        checklistItemId, dto.getChecklistItem());

    List<ChecklistSubItemDto> checklistSubItemDtoList = null;
    if (dto.getChecklistSubItems() != null) {
      List<ChecklistSubItem> modifiedChecklistSubItems = updateChecklistSubItemList(
          dto.getChecklistSubItems());
      checklistSubItemDtoList = modifiedChecklistSubItems.stream()
          .map(ChecklistSubItemDto::new).toList();
    }

    return ChecklistItemApiDto.builder()
        .checklistItem(new ChecklistItemDto(modifiedChecklistItem))
        .checklistSubItems(checklistSubItemDtoList)
        .build();
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public ChecklistItem updateChecklistItem(Long checklistItemId,
      ChecklistItemDto checklistItemDto) {
    Optional<ChecklistItem> checklistItem = checklistItemRepository.findById(
        checklistItemId);

    if (checklistItem.isEmpty()) {
      throw new BadRequestException(MessageUtil.getMessage("checklist.item.notFound.exception"));
    }
    return checklistItem.get().update(checklistItemDto);
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public List<ChecklistSubItem> updateChecklistSubItemList(
      List<ChecklistSubItemDto> checklistSubItemDtoList) {
    return checklistSubItemDtoList.stream().map(this::updateChecklistSubItem).toList();
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public ChecklistSubItem updateChecklistSubItem(ChecklistSubItemDto checklistSubItemDto) {
    Optional<ChecklistSubItem> checklistSubItem = checklistSubItemRepository.findById(
        checklistSubItemDto.getId());

    if (checklistSubItem.isEmpty()) {
      throw new BadRequestException(
          MessageUtil.getMessage("checklist.subItem.notFound.exception"));
    }
    return checklistSubItem.get().update(checklistSubItemDto);
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public boolean withdrawChecklistItem(Long id) {
    return checklistItemRepository.findById(id)
        .map(checklistItem -> {
          checklistItemRepository.delete(checklistItem);
          return true;
        })
        .orElse(false);
  }

  @Override
  @Transactional(readOnly = true)
  public ChecklistItem findChecklistItem(Long checklistItemId, Long memberId) {
    ChecklistItem checklistItem = checklistItemRepository.findById(checklistItemId).orElseThrow(
        () -> new NotFoundException(
            MessageUtil.getMessage("checklist.item.notFound.exception")));

    if (Objects.equals(checklistItem.getMember().getId(), memberId)) {
      return checklistItem;
    } else {
      throw new ForbiddenException(
          MessageUtil.getMessage("checklist.item.forbidden.exception"));
    }
  }
}
