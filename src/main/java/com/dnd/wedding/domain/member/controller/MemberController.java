package com.dnd.wedding.domain.member.controller;

import com.dnd.wedding.domain.member.Gender;
import com.dnd.wedding.domain.member.service.MemberService;
import com.dnd.wedding.domain.oauth.CustomUserDetails;
import com.dnd.wedding.global.exception.NotFoundException;
import com.dnd.wedding.global.response.SuccessResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/user")
public class MemberController {

  private final MemberService memberService;

  @GetMapping("/gender")
  public ResponseEntity<SuccessResponse> getGender(@AuthenticationPrincipal CustomUserDetails user) {
    Gender gender = memberService.getGender(user.getId())
        .orElseThrow(() -> new NotFoundException("성별 정보가 존재하지 않습니다."));
    return ResponseEntity.ok(new SuccessResponse(gender));
  }
}
