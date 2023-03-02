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

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/checklist/item")
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
        .orElseThrow(() -> new NotFoundException("not found user"));
    ChecklistItemApiDto savedChecklistItem = checklistItemService.createChecklistItem(
        requestDto, member);

    if (savedChecklistItem == null) {
      throw new InternalServerErrorException("체크리스트 아이템 등록에 실패하였습니다.");
    }
    return ResponseEntity.status(HttpStatus.CREATED).body(
        SuccessResponse.builder().httpStatus(HttpStatus.CREATED).message("체크리스트 아이템 등록 성공")
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
        SuccessResponse.builder().message("체크리스트 아이템 수정 성공").data(modifiedChecklistItem).build());
  }

  @DeleteMapping("/{item-id}")
  public ResponseEntity<SuccessResponse> withdrawChecklistItem(
      @AuthenticationPrincipal CustomUserDetails user,
      @PathVariable("item-id") Long checklistItemId) {
    ChecklistItem checklistItem = checklistItemService.findChecklistItem(checklistItemId,
        user.getId());

    boolean result = checklistItemService.withdrawChecklistItem(checklistItem.getId());
    if (!result) {
      throw new NotFoundException("존재하지 않는 체크리스트입니다.");
    }
    return ResponseEntity.ok(SuccessResponse.builder().message("체크리스트 삭제 성공").build());
  }
}
