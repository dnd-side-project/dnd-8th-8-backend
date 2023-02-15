package com.dnd.wedding.domain.checklist.checklistitem.controller;

import com.dnd.wedding.domain.checklist.checklistitem.ChecklistItem;
import com.dnd.wedding.domain.checklist.checklistitem.dto.ChecklistItemApiDto;
import com.dnd.wedding.domain.checklist.checklistitem.dto.ChecklistItemDto;
import com.dnd.wedding.domain.checklist.checklistitem.service.ChecklistItemService;
import com.dnd.wedding.domain.checklist.checklistsubitem.dto.ChecklistSubItemDto;
import com.dnd.wedding.domain.checklist.checklistsubitem.service.ChecklistSubItemService;
import com.dnd.wedding.domain.member.Member;
import com.dnd.wedding.domain.member.MemberRepository;
import com.dnd.wedding.domain.oauth.CustomUserDetails;
import com.dnd.wedding.global.exception.InternalServerErrorException;
import com.dnd.wedding.global.exception.NotFoundException;
import com.dnd.wedding.global.response.SuccessResponse;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
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

  private final MemberRepository memberRepository;
  private final ChecklistItemService checklistItemService;
  private final ChecklistSubItemService checklistSubItemService;

  @GetMapping("/{item-id}")
  public ResponseEntity<SuccessResponse> getChecklistItemDetail(
      @PathVariable("item-id") Long checklistItemId,
      @AuthenticationPrincipal CustomUserDetails user) {
    ChecklistItem checklistItem = checklistItemService.findChecklistItemById(checklistItemId)
        .orElseThrow(() -> new NotFoundException("not found checklist item"));

    List<ChecklistSubItemDto> checklistSubItems = checklistSubItemService.findChecklistSubItems(
        checklistItem.getId());

    ChecklistItemApiDto responseData = new ChecklistItemApiDto(
        new ChecklistItemDto(checklistItem), checklistSubItems);

    return ResponseEntity.ok().body(SuccessResponse.builder().data(responseData).build());
  }

  @PostMapping()
  public ResponseEntity<SuccessResponse> createChecklistItem(
      @Valid @RequestBody ChecklistItemApiDto requestDto,
      @AuthenticationPrincipal CustomUserDetails user) {
    Member member = memberRepository.findById(user.getId())
        .orElseThrow(() -> new NotFoundException("not found user"));
    ChecklistItemApiDto savedChecklistItem = checklistItemService.createChecklistItem(
        requestDto, member);

    if (savedChecklistItem == null) {
      throw new InternalServerErrorException("체크리스트 아이템 등록에 실패하였습니다.");
    }
    return ResponseEntity.ok().body(SuccessResponse.builder().data(savedChecklistItem).build());
  }

  @PutMapping("/{item-id}")
  public ResponseEntity<SuccessResponse> modifyChecklistItem(
      @PathVariable("item-id") Long checklistItemId,
      @Valid @RequestBody ChecklistItemApiDto requestDto,
      @AuthenticationPrincipal CustomUserDetails user) {
    ChecklistItem checklistItem = checklistItemService.findChecklistItemById(checklistItemId)
        .orElseThrow(() -> new NotFoundException("존재하지 않는 체크리스트입니다."));

    ChecklistItemApiDto modifiedChecklistItem =
        checklistItemService.modifyChecklistItem(checklistItem.getId(), requestDto);
    return ResponseEntity.ok().body(SuccessResponse.builder().data(modifiedChecklistItem).build());
  }

  @DeleteMapping("/{item-id}")
  public ResponseEntity<SuccessResponse> withdrawChecklistItem(
      @PathVariable("item-id") Long checklistItemId,
      @AuthenticationPrincipal CustomUserDetails user) {
    boolean result = checklistItemService.withdrawChecklistItem(checklistItemId);
    if (!result) {
      throw new NotFoundException("존재하지 않는 체크리스트입니다.");
    }
    return ResponseEntity.ok(SuccessResponse.builder().message("체크리스트 삭제 성공").build());
  }
}
