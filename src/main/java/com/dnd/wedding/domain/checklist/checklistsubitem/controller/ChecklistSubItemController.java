package com.dnd.wedding.domain.checklist.checklistsubitem.controller;


import com.dnd.wedding.domain.checklist.checklistitem.ChecklistItem;
import com.dnd.wedding.domain.checklist.checklistitem.service.ChecklistItemService;
import com.dnd.wedding.domain.checklist.checklistsubitem.ChecklistSubItem;
import com.dnd.wedding.domain.checklist.checklistsubitem.dto.ChecklistSubItemDto;
import com.dnd.wedding.domain.checklist.checklistsubitem.dto.UpdateChecklistSubItemDto;
import com.dnd.wedding.domain.checklist.checklistsubitem.service.ChecklistSubItemService;
import com.dnd.wedding.domain.oauth.CustomUserDetails;
import com.dnd.wedding.global.exception.BadRequestException;
import com.dnd.wedding.global.exception.InternalServerErrorException;
import com.dnd.wedding.global.exception.NotFoundException;
import com.dnd.wedding.global.response.SuccessResponse;
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

  private static final String NOT_FOUND_CHECKLIST_MESSAGE = "존재하지 않는 체크리스트입니다.";
  private final ChecklistItemService checklistItemService;
  private final ChecklistSubItemService checklistSubItemService;

  @PostMapping
  public ResponseEntity<SuccessResponse> createChecklistSubItem(
      @AuthenticationPrincipal CustomUserDetails user,
      @PathVariable("item-id") Long checklistItemId,
      @RequestBody @Valid ChecklistSubItemDto checklistSubItemDto) {
    ChecklistItem checklistItem = checklistItemService.findChecklistItemById(checklistItemId)
        .orElseThrow(() -> new NotFoundException(NOT_FOUND_CHECKLIST_MESSAGE));

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
    ChecklistItem checklistItem = checklistItemService.findChecklistItemById(checklistItemId)
        .orElseThrow(() -> new NotFoundException(NOT_FOUND_CHECKLIST_MESSAGE));

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
      @RequestBody @Valid UpdateChecklistSubItemDto checklistSubItemDto) {
    ChecklistItem checklistItem = checklistItemService.findChecklistItemById(checklistItemId)
        .orElseThrow(() -> new NotFoundException(NOT_FOUND_CHECKLIST_MESSAGE));

    ChecklistSubItem modifiedChecklistSubItem = checklistSubItemService.modifyChecklistSubItem(
        checklistSubItemId,
        checklistItem.getId(), checklistSubItemDto);
    if (modifiedChecklistSubItem == null) {
      throw new BadRequestException("체크리스트 아이템과 요청한 서브 아이템이 매칭되지 않습니다.");
    }
    return ResponseEntity.ok(
        SuccessResponse.builder().message("체크리스트 서브 아이템 체크 여부 수정 성공")
            .data(new ChecklistSubItemDto(modifiedChecklistSubItem)).build());
  }
}
