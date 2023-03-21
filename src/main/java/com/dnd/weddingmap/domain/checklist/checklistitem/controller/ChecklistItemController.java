package com.dnd.weddingmap.domain.checklist.checklistitem.controller;

import com.dnd.weddingmap.domain.checklist.checklistitem.ChecklistItem;
import com.dnd.weddingmap.domain.checklist.checklistitem.dto.ChecklistItemApiDto;
import com.dnd.weddingmap.domain.checklist.checklistitem.dto.ChecklistItemDto;
import com.dnd.weddingmap.domain.checklist.checklistitem.service.ChecklistItemService;
import com.dnd.weddingmap.domain.checklist.checklistsubitem.dto.ChecklistSubItemDto;
import com.dnd.weddingmap.domain.checklist.checklistsubitem.service.ChecklistSubItemService;
import com.dnd.weddingmap.domain.member.Member;
import com.dnd.weddingmap.domain.member.service.MemberService;
import com.dnd.weddingmap.domain.oauth.CustomUserDetails;
import com.dnd.weddingmap.global.exception.InternalServerErrorException;
import com.dnd.weddingmap.global.exception.NotFoundException;
import com.dnd.weddingmap.global.response.SuccessResponse;
import com.dnd.weddingmap.global.util.MessageUtil;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/checklist/item")
@RequiredArgsConstructor
public class ChecklistItemController {

  private final MemberService memberService;
  private final ChecklistItemService checklistItemService;
  private final ChecklistSubItemService checklistSubItemService;

  @GetMapping("/{item-id}")
  public ResponseEntity<SuccessResponse> getChecklistItemDetail(
      @AuthenticationPrincipal CustomUserDetails user,
      @PathVariable("item-id") Long checklistItemId) {
    ChecklistItem checklistItem = checklistItemService.findChecklistItem(checklistItemId,
        user.getId());

    List<ChecklistSubItemDto> checklistSubItemDtoList =
        checklistSubItemService.findChecklistSubItems(checklistItem.getId());

    ChecklistItemApiDto responseData = new ChecklistItemApiDto(
        new ChecklistItemDto(checklistItem), checklistSubItemDtoList);

    return ResponseEntity.ok().body(SuccessResponse.builder().data(responseData).build());
  }

  @PostMapping
  public ResponseEntity<SuccessResponse> createChecklistItem(
      @AuthenticationPrincipal CustomUserDetails user,
      @RequestBody @Valid ChecklistItemApiDto requestDto) {
    Member member = memberService.findMember(user.getId())
        .orElseThrow(
            () -> new NotFoundException(MessageUtil.getMessage("notFound.user.exception.msg")));
    ChecklistItemApiDto savedChecklistItem = checklistItemService.createChecklistItem(
        requestDto, member);

    if (savedChecklistItem == null) {
      throw new InternalServerErrorException(
          MessageUtil.getMessage("failure.createChecklistItem.exception.msg"));
    }
    return ResponseEntity.status(HttpStatus.CREATED).body(
        SuccessResponse.builder().httpStatus(HttpStatus.CREATED)
            .message(MessageUtil.getMessage("success.createChecklistItem.msg"))
            .data(savedChecklistItem).build());
  }

  @PutMapping("/{item-id}")
  public ResponseEntity<SuccessResponse> modifyChecklistItem(
      @AuthenticationPrincipal CustomUserDetails user,
      @PathVariable("item-id") Long checklistItemId,
      @RequestBody @Valid ChecklistItemApiDto requestDto) {
    ChecklistItem checklistItem = checklistItemService.findChecklistItem(checklistItemId,
        user.getId());

    ChecklistItemApiDto modifiedChecklistItem =
        checklistItemService.modifyChecklistItem(checklistItem.getId(), requestDto);
    return ResponseEntity.ok().body(
        SuccessResponse.builder().message(MessageUtil.getMessage("success.modifyChecklistItem.msg"))
            .data(modifiedChecklistItem).build());
  }

  @DeleteMapping("/{item-id}")
  public ResponseEntity<SuccessResponse> withdrawChecklistItem(
      @AuthenticationPrincipal CustomUserDetails user,
      @PathVariable("item-id") Long checklistItemId) {
    ChecklistItem checklistItem = checklistItemService.findChecklistItem(checklistItemId,
        user.getId());

    boolean result = checklistItemService.withdrawChecklistItem(checklistItem.getId());
    if (!result) {
      throw new NotFoundException(MessageUtil.getMessage("notFound.checklistItem.exception.msg"));
    }
    return ResponseEntity.ok(SuccessResponse.builder()
        .message(MessageUtil.getMessage("success.withdrawChecklistItem.msg")).build());
  }
}
