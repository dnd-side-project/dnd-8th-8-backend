package com.dnd.weddingmap.domain.checklist.controller;

import com.dnd.weddingmap.domain.checklist.checklistitem.dto.ChecklistItemApiDto;
import com.dnd.weddingmap.domain.checklist.checklistitem.dto.ChecklistItemDto;
import com.dnd.weddingmap.domain.checklist.dto.PreChecklistItemListDto;
import com.dnd.weddingmap.domain.checklist.service.ChecklistService;
import com.dnd.weddingmap.domain.member.Member;
import com.dnd.weddingmap.domain.member.service.MemberService;
import com.dnd.weddingmap.domain.oauth.CustomUserDetails;
import com.dnd.weddingmap.global.exception.NotFoundException;
import com.dnd.weddingmap.global.response.SuccessResponse;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/checklist")
public class ChecklistController {

  private final MemberService memberService;
  private final ChecklistService checklistService;

  @GetMapping
  public ResponseEntity<SuccessResponse> getChecklist(
      @AuthenticationPrincipal CustomUserDetails user) {
    Member member = memberService.findMember(user.getId())
        .orElseThrow(() -> new NotFoundException("존재하지 않는 사용자입니다."));

    List<ChecklistItemApiDto> result = checklistService.findChecklist(member.getId());
    return ResponseEntity.ok().body(SuccessResponse.builder().data(result).build());
  }

  @PostMapping("/pre-check")
  public ResponseEntity<SuccessResponse> savePreChecklists(
      @AuthenticationPrincipal CustomUserDetails user,
      @RequestBody @Valid PreChecklistItemListDto preChecklistItemListDto) {
    Member member = memberService.findMember(user.getId())
        .orElseThrow(() -> new NotFoundException("존재하지 않는 사용자입니다."));

    List<ChecklistItemDto> result = checklistService.savePreChecklistItemList(member,
        preChecklistItemListDto);

    return ResponseEntity.status(HttpStatus.CREATED).body(
        SuccessResponse.builder().httpStatus(HttpStatus.CREATED).message("체크리스트 사전 등록 성공")
            .data(result).build());
  }
}
