package com.dnd.wedding.domain.checklist.checklistsubitem.controller;


import com.dnd.wedding.domain.checklist.checklistitem.ChecklistItem;
import com.dnd.wedding.domain.checklist.checklistitem.service.ChecklistItemService;
import com.dnd.wedding.domain.checklist.checklistsubitem.ChecklistSubItem;
import com.dnd.wedding.domain.checklist.checklistsubitem.dto.ChecklistSubItemDto;
import com.dnd.wedding.domain.checklist.checklistsubitem.service.ChecklistSubItemService;
import com.dnd.wedding.domain.oauth.CustomUserDetails;
import com.dnd.wedding.global.exception.InternalServerErrorException;
import com.dnd.wedding.global.exception.NotFoundException;
import com.dnd.wedding.global.response.SuccessResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/checklist/item/{item-id}/sub-item")
public class ChecklistSubItemController {

  private final ChecklistItemService checklistItemService;
  private final ChecklistSubItemService checklistSubItemService;

  @PostMapping()
  public ResponseEntity<SuccessResponse> createChecklistSubItem(
      @PathVariable("item-id") Long checklistItemId,
      @RequestBody ChecklistSubItemDto checklistSubItemDto,
      @AuthenticationPrincipal CustomUserDetails user) {
    ChecklistItem checklistItem = checklistItemService.findChecklistItemById(checklistItemId)
        .orElseThrow(() -> new NotFoundException("존재하지 않는 체크리스트입니다."));

    ChecklistSubItem checklistSubItem = checklistSubItemService.saveChecklistSubItem(
        checklistSubItemDto, checklistItem);
    if (checklistSubItem == null) {
      throw new InternalServerErrorException("체크리스트 서브 아이템 등록에 실패하였습니다.");
    }
    return ResponseEntity.ok()
        .body(SuccessResponse.builder().data(new ChecklistSubItemDto(checklistSubItem)).build());
  }

  @DeleteMapping("/{sub-item-id}")
  public ResponseEntity<SuccessResponse> withdrawChecklistSubItem(
      @PathVariable("item-id") Long checklistItemId,
      @PathVariable("sub-item-id") Long checklistSubItemId,
      @AuthenticationPrincipal CustomUserDetails user) {
    ChecklistItem checklistItem = checklistItemService.findChecklistItemById(checklistItemId)
        .orElseThrow(() -> new NotFoundException("존재하지 않는 체크리스트입니다."));

    boolean result = checklistSubItemService.withdrawChecklistSubItem(checklistSubItemId,
        checklistItem.getId());
    if (!result) {
      throw new NotFoundException("체크리스트 서브 아이템 삭제에 실패하였습니다.");
    }
    return ResponseEntity.ok(SuccessResponse.builder().message("체크리스트 서브 아이템 삭제 성공").build());
  }
}
