package com.dnd.weddingmap.domain.checklist.checklistsubitem.controller;


import com.dnd.weddingmap.domain.checklist.checklistitem.ChecklistItem;
import com.dnd.weddingmap.domain.checklist.checklistitem.service.ChecklistItemService;
import com.dnd.weddingmap.domain.checklist.checklistsubitem.ChecklistSubItem;
import com.dnd.weddingmap.domain.checklist.checklistsubitem.dto.ChecklistSubItemDto;
import com.dnd.weddingmap.domain.checklist.checklistsubitem.dto.ChecklistSubItemStateDto;
import com.dnd.weddingmap.domain.checklist.checklistsubitem.service.ChecklistSubItemService;
import com.dnd.weddingmap.domain.oauth.CustomUserDetails;
import com.dnd.weddingmap.global.exception.BadRequestException;
import com.dnd.weddingmap.global.exception.InternalServerErrorException;
import com.dnd.weddingmap.global.exception.NotFoundException;
import com.dnd.weddingmap.global.response.SuccessResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/checklist/item/{item-id}/sub-item")
public class ChecklistSubItemController {

  private final ChecklistItemService checklistItemService;
  private final ChecklistSubItemService checklistSubItemService;

  @PostMapping
  public ResponseEntity<SuccessResponse> createChecklistSubItem(
      @AuthenticationPrincipal CustomUserDetails user,
      @PathVariable("item-id") Long checklistItemId,
      @RequestBody @Valid ChecklistSubItemDto checklistSubItemDto) {
    ChecklistItem checklistItem = checklistItemService.findChecklistItem(checklistItemId,
        user.getId());

    ChecklistSubItem checklistSubItem = checklistSubItemService.saveChecklistSubItem(
        checklistSubItemDto, checklistItem);
    if (checklistSubItem == null) {
      throw new InternalServerErrorException("체크리스트 서브 아이템 등록에 실패하였습니다.");
    }
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(SuccessResponse.builder().httpStatus(HttpStatus.CREATED).message("체크리스트 서브 아이템 등록 성공")
            .data(new ChecklistSubItemDto(checklistSubItem)).build());
  }

  @DeleteMapping("/{sub-item-id}")
  public ResponseEntity<SuccessResponse> withdrawChecklistSubItem(
      @AuthenticationPrincipal CustomUserDetails user,
      @PathVariable("item-id") Long checklistItemId,
      @PathVariable("sub-item-id") Long checklistSubItemId) {
    ChecklistItem checklistItem = checklistItemService.findChecklistItem(checklistItemId,
        user.getId());

    boolean result = checklistSubItemService.withdrawChecklistSubItem(checklistSubItemId,
        checklistItem.getId());
    if (!result) {
      throw new NotFoundException("체크리스트 서브 아이템 삭제에 실패하였습니다.");
    }
    return ResponseEntity.ok(SuccessResponse.builder().message("체크리스트 서브 아이템 삭제 성공").build());
  }

  @PutMapping("/{sub-item-id}")
  public ResponseEntity<SuccessResponse> modifyChecklistSubItem(
      @AuthenticationPrincipal CustomUserDetails user,
      @PathVariable("item-id") Long checklistItemId,
      @PathVariable("sub-item-id") Long checklistSubItemId,
      @RequestBody @Valid ChecklistSubItemStateDto checklistSubItemStateDto) {
    ChecklistItem checklistItem = checklistItemService.findChecklistItem(checklistItemId,
        user.getId());

    ChecklistSubItem modifiedChecklistSubItem = checklistSubItemService.modifyChecklistSubItem(
        checklistSubItemId, checklistItem.getId(), checklistSubItemStateDto);
    if (modifiedChecklistSubItem == null) {
      throw new BadRequestException("체크리스트 아이템과 요청한 서브 아이템이 매칭되지 않습니다.");
    }
    return ResponseEntity.ok(
        SuccessResponse.builder().message("체크리스트 서브 아이템 체크 여부 수정 성공")
            .data(new ChecklistSubItemDto(modifiedChecklistSubItem)).build());
  }
}
