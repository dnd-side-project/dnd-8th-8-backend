package com.dnd.wedding.domain.member.controller;

import com.dnd.wedding.domain.member.Gender;
import com.dnd.wedding.domain.member.dto.GenderDto;
import com.dnd.wedding.domain.member.dto.ProfileImageDto;
import com.dnd.wedding.domain.member.service.MemberService;
import com.dnd.wedding.domain.oauth.CustomUserDetails;
import com.dnd.wedding.global.exception.NotFoundException;
import com.dnd.wedding.global.response.SuccessResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/user")
public class MemberController {

  private final MemberService memberService;

  @GetMapping("/gender")
  public ResponseEntity<SuccessResponse> getGender(
      @AuthenticationPrincipal CustomUserDetails user) {
    Gender gender = memberService.getGender(user.getId())
        .orElseThrow(() -> new NotFoundException("성별 정보가 존재하지 않습니다."));

    return ResponseEntity.ok(SuccessResponse.builder()
        .data(new GenderDto(gender))
        .build());
  }

  @PostMapping("/gender")
  public ResponseEntity<SuccessResponse> postGender(
      @AuthenticationPrincipal CustomUserDetails user, @RequestBody GenderDto dto
  ) {
    memberService.postGender(user.getId(), dto.getGender());
    return ResponseEntity.ok(SuccessResponse.builder().message("성별 정보 등록 성공").build());
  }

  @GetMapping("/profile")
  public ResponseEntity<SuccessResponse> getProfileImage(
      @AuthenticationPrincipal CustomUserDetails user) {
    String profileImage = memberService.getProfileImage(user.getId())
        .orElseThrow(() -> new NotFoundException("프로필 이미지가 존재하지 않습니다."));
    return ResponseEntity.ok(SuccessResponse.builder().data(profileImage).build());
  }

  @PutMapping("/profile")
  public ResponseEntity<SuccessResponse> putProfileImage(
      @AuthenticationPrincipal CustomUserDetails user, @RequestBody ProfileImageDto dto) {
    memberService.putProfileImage(user.getId(), dto.getUrl());
    return ResponseEntity.ok(SuccessResponse.builder().message("프로필 이미지 등록 성공").build());
  }

  @DeleteMapping
  public ResponseEntity<SuccessResponse> withdraw(
      @AuthenticationPrincipal CustomUserDetails user) {
    boolean result = memberService.withdraw(user.getId());
    if (!result) {
      throw new NotFoundException("회원 정보가 존재하지 않습니다.");
    }
    return ResponseEntity.ok(SuccessResponse.builder().message("회원 탈퇴 성공").build());
  }
}
