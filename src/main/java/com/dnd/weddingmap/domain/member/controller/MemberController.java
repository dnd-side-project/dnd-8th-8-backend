package com.dnd.weddingmap.domain.member.controller;

import com.dnd.weddingmap.domain.member.Gender;
import com.dnd.weddingmap.domain.member.dto.GenderDto;
import com.dnd.weddingmap.domain.member.dto.NameDto;
import com.dnd.weddingmap.domain.member.dto.ProfileImageDto;
import com.dnd.weddingmap.domain.member.service.MemberService;
import com.dnd.weddingmap.domain.oauth.CustomUserDetails;
import com.dnd.weddingmap.global.exception.NotFoundException;
import com.dnd.weddingmap.global.exception.RequestTimeoutException;
import com.dnd.weddingmap.global.response.SuccessResponse;
import com.dnd.weddingmap.global.service.S3Service;
import com.dnd.weddingmap.global.util.MessageUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class MemberController {

  private final MemberService memberService;
  private final S3Service s3Service;

  @PutMapping("/name")
  public ResponseEntity<SuccessResponse> modifyName(
      @AuthenticationPrincipal CustomUserDetails user,
      @RequestBody @Valid NameDto nameDto) {

    memberService.modifyName(user.getId(), nameDto);
    return ResponseEntity.ok(
        SuccessResponse.builder().message(MessageUtil.getMessage("member.modifyName.success"))
            .build());
  }

  @GetMapping("/gender")
  public ResponseEntity<SuccessResponse> getGender(
      @AuthenticationPrincipal CustomUserDetails user) {

    Gender gender = memberService.getGender(user.getId())
        .orElseThrow(
            () -> new NotFoundException(MessageUtil.getMessage("member.notFoundGender.exception")));

    return ResponseEntity.ok(SuccessResponse.builder()
        .data(new GenderDto(gender))
        .build());
  }

  @PostMapping("/gender")
  public ResponseEntity<SuccessResponse> postGender(
      @AuthenticationPrincipal CustomUserDetails user,
      @RequestBody @Valid GenderDto dto) {

    memberService.postGender(user.getId(), dto.getGender());
    return ResponseEntity.ok(
        SuccessResponse.builder().message(MessageUtil.getMessage("member.registerGender.success"))
            .build());
  }

  @GetMapping("/profile")
  public ResponseEntity<SuccessResponse> getProfileImage(
      @AuthenticationPrincipal CustomUserDetails user) {

    String profileImage = memberService.getProfileImage(user.getId())
        .orElseThrow(() -> new NotFoundException(
            MessageUtil.getMessage("member.notFoundProfileImage.exception")));

    return ResponseEntity.ok(SuccessResponse.builder()
        .data(new ProfileImageDto(profileImage))
        .build());
  }

  @PostMapping("/profile")
  public ResponseEntity<SuccessResponse> postProfileImage(
      @AuthenticationPrincipal CustomUserDetails user,
      @RequestParam("image") MultipartFile imageFile) {

    String imageUrl;

    try {
      imageUrl = s3Service.upload(imageFile, "profile");
    } catch (Exception e) {
      throw new RequestTimeoutException(
          MessageUtil.getMessage("member.uploadProfileImage.failure"));
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
      throw new NotFoundException(MessageUtil.getMessage("member.notFoundUserInfo.exception"));
    }
    return ResponseEntity.ok(
        SuccessResponse.builder().message(MessageUtil.getMessage("member.withdraw.success"))
            .build());
  }
}
