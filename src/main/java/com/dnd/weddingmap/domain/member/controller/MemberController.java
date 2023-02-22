package com.dnd.weddingmap.domain.member.controller;

import com.dnd.weddingmap.domain.member.Gender;
import com.dnd.weddingmap.domain.member.dto.GenderDto;
import com.dnd.weddingmap.domain.member.dto.ProfileImageDto;
import com.dnd.weddingmap.domain.member.service.MemberService;
import com.dnd.weddingmap.domain.oauth.CustomUserDetails;
import com.dnd.weddingmap.global.exception.NotFoundException;
import com.dnd.weddingmap.global.exception.RequestTimeoutException;
import com.dnd.weddingmap.global.response.SuccessResponse;
import com.dnd.weddingmap.global.service.S3Service;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/user")
public class MemberController {

  private final MemberService memberService;
  private final S3Service s3Service;

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
      @AuthenticationPrincipal CustomUserDetails user,
      @RequestBody @Valid GenderDto dto) {

    memberService.postGender(user.getId(), dto.getGender());
    return ResponseEntity.ok(SuccessResponse.builder().message("성별 정보 등록 성공").build());
  }

  @GetMapping("/profile")
  public ResponseEntity<SuccessResponse> getProfileImage(
      @AuthenticationPrincipal CustomUserDetails user) {

    String profileImage = memberService.getProfileImage(user.getId())
        .orElseThrow(() -> new NotFoundException("프로필 이미지가 존재하지 않습니다."));

    return ResponseEntity.ok(SuccessResponse.builder()
        .data(new ProfileImageDto(profileImage))
        .build());
  }

  @PostMapping("/profile")
  public ResponseEntity<SuccessResponse> putProfileImage(
      @AuthenticationPrincipal CustomUserDetails user,
      @RequestParam("image") MultipartFile imageFile) {

    String imageUrl;

    try {
      imageUrl = s3Service.upload(imageFile, "profile");
    } catch (Exception e) {
      throw new RequestTimeoutException("이미지 업로드에 실패했습니다.");
    }
    memberService.putProfileImage(user.getId(), imageUrl);

    return ResponseEntity.ok(SuccessResponse.builder()
        .data(new ProfileImageDto(imageUrl))
        .build());
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
