package com.dnd.weddingmap.domain.contract.controller;

import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.dnd.weddingmap.domain.contract.Contract;
import com.dnd.weddingmap.domain.contract.dto.ContractDto;
import com.dnd.weddingmap.domain.contract.dto.ContractListResponseDto;
import com.dnd.weddingmap.domain.contract.service.ContractService;
import com.dnd.weddingmap.domain.member.Member;
import com.dnd.weddingmap.domain.member.service.MemberService;
import com.dnd.weddingmap.domain.oauth.CustomUserDetails;
import com.dnd.weddingmap.global.exception.ForbiddenException;
import com.dnd.weddingmap.global.exception.NotFoundException;
import com.dnd.weddingmap.global.exception.RequestTimeoutException;
import com.dnd.weddingmap.global.response.SuccessResponse;
import com.dnd.weddingmap.global.service.S3Service;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Objects;
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
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;


@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/contract")
public class ContractController {

  private static final String NOT_FOUND_CONTRACT_MESSAGE = "존재하지 않는 계약서입니다.";
  private static final String CONTRACT_DIRECTORY = "contract";
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
      fileUrl = s3Service.upload(file, CONTRACT_DIRECTORY);
    } catch (Exception e) {
      throw new RequestTimeoutException("계약서 파일 업로드에 실패했습니다.");
    }
    requestDto.setFile(fileUrl);
    ContractDto savedContract = contractService.createContract(requestDto, member);

    return ResponseEntity.status(HttpStatus.CREATED).body(
        SuccessResponse.builder().httpStatus(HttpStatus.CREATED).message("계약서 등록 성공")
            .data(savedContract).build());
  }

  @GetMapping("/{contract-id}")
  public ResponseEntity<SuccessResponse> getContractDetail(
      @AuthenticationPrincipal CustomUserDetails user,
      @PathVariable("contract-id") Long contractId) {
    Contract contract = checkPermission(contractId, user.getId());

    return ResponseEntity.ok()
        .body(SuccessResponse.builder().data(new ContractDto(contract)).build());
  }

  @DeleteMapping("/{contract-id}")
  public ResponseEntity<SuccessResponse> withdrawContract(
      @AuthenticationPrincipal CustomUserDetails user,
      @PathVariable("contract-id") Long contractId) {
    Contract contract = checkPermission(contractId, user.getId());

    try {
      s3Service.delete(contract.getFile(), CONTRACT_DIRECTORY);
    } catch (Exception e) {
      throw new AmazonS3Exception("계약서 파일 삭제에 실패했습니다.");
    }

    boolean result = contractService.withdrawContract(contractId);
    if (!result) {
      throw new NotFoundException(NOT_FOUND_CONTRACT_MESSAGE);
    }
    return ResponseEntity.ok(SuccessResponse.builder().message("계약서 삭제 성공").build());
  }

  @GetMapping
  public ResponseEntity<SuccessResponse> getContractList(
      @AuthenticationPrincipal CustomUserDetails user) {
    Member member = memberService.findMember(user.getId())
        .orElseThrow(() -> new NotFoundException("존재하지 않는 사용자입니다."));

    List<ContractListResponseDto> contractList = contractService.findContractList(member.getId());

    return ResponseEntity.ok()
        .body(SuccessResponse.builder().data(contractList).build());
  }

  @PostMapping("/{contract-id}")
  public ResponseEntity<SuccessResponse> modifyContractFile(
      @AuthenticationPrincipal CustomUserDetails user,
      @PathVariable("contract-id") Long contractId,
      @RequestPart("file") MultipartFile file) {
    Contract contract = checkPermission(contractId, user.getId());

    try {
      s3Service.delete(contract.getFile(), CONTRACT_DIRECTORY);
    } catch (Exception e) {
      throw new AmazonS3Exception("계약서 파일 삭제에 실패했습니다.");
    }

    String fileUrl;
    try {
      fileUrl = s3Service.upload(file, CONTRACT_DIRECTORY);
    } catch (Exception e) {
      throw new RequestTimeoutException("계약서 파일 수정에 실패했습니다.");
    }

    ContractDto result = contractService.modifyContractFile(contractId, fileUrl);
    return ResponseEntity.ok()
        .body(SuccessResponse.builder().message("계약서 파일 수정 성공").data(result).build());
  }

  @PutMapping("/{contract-id}")
  public ResponseEntity<SuccessResponse> modifyContract(
      @AuthenticationPrincipal CustomUserDetails user,
      @PathVariable("contract-id") Long contractId,
      @RequestBody @Valid ContractDto requestDto) {
    Contract contract = checkPermission(contractId, user.getId());

    ContractDto result = contractService.modifyContract(contract.getId(), requestDto);
    return ResponseEntity.ok()
        .body(SuccessResponse.builder().message("계약서 수정 성공").data(result).build());
  }

  private Contract checkPermission(Long contractId, Long memberId) {
    Contract contract = contractService.findContractById(contractId)
        .orElseThrow(() -> new NotFoundException(NOT_FOUND_CONTRACT_MESSAGE));

    if (Objects.equals(contract.getMember().getId(), memberId)) {
      return contract;
    } else {
      throw new ForbiddenException("접근할 수 없는 계약서입니다.");
    }
  }
}
