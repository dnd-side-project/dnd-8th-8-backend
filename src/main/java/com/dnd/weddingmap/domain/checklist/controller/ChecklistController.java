package com.dnd.weddingmap.domain.checklist.controller;

import com.dnd.weddingmap.domain.checklist.checklistitem.dto.ChecklistItemApiDto;
import com.dnd.weddingmap.domain.checklist.service.ChecklistService;
import com.dnd.weddingmap.domain.member.Member;
import com.dnd.weddingmap.domain.member.service.MemberService;
import com.dnd.weddingmap.domain.oauth.CustomUserDetails;
import com.dnd.weddingmap.global.exception.NotFoundException;
import com.dnd.weddingmap.global.response.SuccessResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
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
}
