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
import com.dnd.weddingmap.global.util.MessageUtil;
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

@RestController
@RequestMapping("/api/v1/checklist/item/{item-id}/sub-item")
@RequiredArgsConstructor
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
      throw new InternalServerErrorException(
          MessageUtil.getMessage("checklist.subItem.register.failure"));
    }
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(SuccessResponse.builder().httpStatus(HttpStatus.CREATED)
            .message(MessageUtil.getMessage("checklist.subItem.register.success"))
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
      throw new NotFoundException(
          MessageUtil.getMessage("checklist.subItem.withdraw.failure"));
    }
    return ResponseEntity.ok(SuccessResponse.builder()
        .message(MessageUtil.getMessage("checklist.subItem.withdraw.success")).build());
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
      throw new BadRequestException(
          MessageUtil.getMessage("checklist.subItem.notMatching.exception"));
    }
    return ResponseEntity.ok(
        SuccessResponse.builder()
            .message(MessageUtil.getMessage("checklist.subItem.modify.success"))
            .data(new ChecklistSubItemDto(modifiedChecklistSubItem)).build());
  }
}
