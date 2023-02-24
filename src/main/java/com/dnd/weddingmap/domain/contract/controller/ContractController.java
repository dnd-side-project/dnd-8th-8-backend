package com.dnd.weddingmap.domain.contract.controller;

import com.dnd.weddingmap.domain.contract.dto.ContractDto;
import com.dnd.weddingmap.domain.contract.service.ContractService;
import com.dnd.weddingmap.domain.member.Member;
import com.dnd.weddingmap.domain.member.service.MemberService;
import com.dnd.weddingmap.domain.oauth.CustomUserDetails;
import com.dnd.weddingmap.global.exception.NotFoundException;
import com.dnd.weddingmap.global.exception.RequestTimeoutException;
import com.dnd.weddingmap.global.response.SuccessResponse;
import com.dnd.weddingmap.global.service.S3Service;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;


@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/contract")
public class ContractController {

  private final MemberService memberService;
  private final ContractService contractService;
  private final S3Service s3Service;

  @PostMapping
  public ResponseEntity<SuccessResponse> createContract(
      @AuthenticationPrincipal CustomUserDetails user,
      @RequestPart("data") @Valid ContractDto requestDto,
      @RequestPart("file") MultipartFile file) {
    Member member = memberService.findMember(user.getId())
        .orElseThrow(() -> new NotFoundException("존재하지 않는 사용자입니다."));

    String fileUrl;
    try {
      fileUrl = s3Service.upload(file, "contract");
    } catch (Exception e) {
      throw new RequestTimeoutException("계약서 파일 업로드에 실패했습니다.");
    }
    requestDto.setFile(fileUrl);
    ContractDto savedContract = contractService.createContract(requestDto, member);

    return ResponseEntity.status(HttpStatus.CREATED).body(
        SuccessResponse.builder().httpStatus(HttpStatus.CREATED).message("계약서 등록 성공")
            .data(savedContract).build());
  }
}
