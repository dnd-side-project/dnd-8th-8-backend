package com.dnd.wedding.domain.checklist.controller;

import com.dnd.wedding.domain.checklist.checklistitem.dto.ChecklistItemApiDto;
import com.dnd.wedding.domain.checklist.service.ChecklistService;
import com.dnd.wedding.domain.member.Member;
import com.dnd.wedding.domain.member.MemberRepository;
import com.dnd.wedding.domain.oauth.CustomUserDetails;
import com.dnd.wedding.global.exception.NotFoundException;
import com.dnd.wedding.global.response.SuccessResponse;
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

  private final MemberRepository memberRepository;
  private final ChecklistService checklistService;

  @GetMapping
  public ResponseEntity<SuccessResponse> getChecklist(
      @AuthenticationPrincipal CustomUserDetails user) {
    Member member = memberRepository.findById(user.getId())
        .orElseThrow(() -> new NotFoundException("존재하지 않는 사용자입니다."));

    List<ChecklistItemApiDto> result = checklistService.findChecklist(member.getId());
    return ResponseEntity.ok().body(SuccessResponse.builder().data(result).build());
  }
}
