package com.dnd.wedding.domain.checklist.checklistitem.controller;

import com.dnd.wedding.domain.checklist.checklistitem.ChecklistItem;
import com.dnd.wedding.domain.checklist.checklistitem.dto.ChecklistItemDto;
import com.dnd.wedding.domain.checklist.checklistitem.dto.ChecklistItemResponseDto;
import com.dnd.wedding.domain.checklist.checklistitem.service.ChecklistItemService;
import com.dnd.wedding.domain.checklist.checklistsubitem.ChecklistSubItem;
import com.dnd.wedding.domain.checklist.checklistsubitem.dto.ChecklistSubItemDto;
import com.dnd.wedding.domain.checklist.checklistsubitem.service.ChecklistSubItemService;
import com.dnd.wedding.domain.oauth.CustomUserDetails;
import com.dnd.wedding.global.exception.NotFoundException;
import com.dnd.wedding.global.response.SuccessResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/checklist/item")
public class ChecklistItemController {

  private final ChecklistItemService checklistItemService;
  private final ChecklistSubItemService checklistSubItemService;

  @GetMapping("/{item-id}")
  public ResponseEntity<SuccessResponse> getChecklistItemDetail(
      @PathVariable("item-id") Long checklistItemId,
      @AuthenticationPrincipal CustomUserDetails user) {
    ChecklistItem checklistItem = checklistItemService.findChecklistItemById(checklistItemId)
        .orElseThrow(() -> new NotFoundException("not found checklist item"));

    List<ChecklistSubItem> checklistSubItems = checklistSubItemService.findChecklistSubItems(
        checklistItem.getId());
    List<ChecklistSubItemDto> checklistSubItemDtoList = checklistSubItems.stream()
        .map(ChecklistSubItemDto::new).toList();
    ChecklistItemResponseDto responseData = new ChecklistItemResponseDto(
        new ChecklistItemDto(checklistItem), checklistSubItemDtoList);

    return ResponseEntity.ok().body(SuccessResponse.builder().data(responseData).build());
  }
}
