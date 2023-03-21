package com.dnd.weddingmap.domain.contract.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.dnd.weddingmap.domain.contract.Contract;
import com.dnd.weddingmap.domain.contract.ContractStatus;
import com.dnd.weddingmap.domain.contract.dto.ContractDto;
import com.dnd.weddingmap.domain.contract.dto.ContractListResponseDto;
import com.dnd.weddingmap.domain.contract.repository.ContractRepository;
import com.dnd.weddingmap.domain.contract.service.impl.ContractServiceImpl;
import com.dnd.weddingmap.domain.member.Member;
import com.dnd.weddingmap.domain.member.Role;
import com.dnd.weddingmap.domain.oauth.OAuth2Provider;
import com.dnd.weddingmap.global.exception.NotFoundException;
import com.dnd.weddingmap.global.util.MessageUtil;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;

@ExtendWith(MockitoExtension.class)
class ContractServiceTest {

  Member member;
  Contract contract;
  ContractDto contractDto;
  Long memberId = 1L;
  Long contractId = 1L;

  @InjectMocks
  ContractServiceImpl contractService;
  @Mock
  ContractRepository contractRepository;

  @Mock
  MessageSource messageSource;

  @BeforeEach
  void init() {
    member = Member.builder()
        .id(memberId)
        .name("test")
        .email("test@example.com")
        .profileImage("test.png")
        .role(Role.USER)
        .oauth2Provider(OAuth2Provider.GOOGLE)
        .build();

    contract = Contract.builder()
        .id(contractId)
        .title("test_title")
        .contents("test_contents")
        .contractDate(LocalDate.parse("2022-12-12"))
        .contractStatus(ContractStatus.PROVISIONAL)
        .file("test_file_url")
        .memo("test_memo")
        .member(member)
        .build();

    contractDto = ContractDto.builder()
        .title("test_title")
        .contents("test_contents")
        .contractDate(LocalDate.parse("2022-12-12"))
        .contractStatus(ContractStatus.PROVISIONAL)
        .file("test_file_url")
        .memo("test_memo")
        .build();
    MessageUtil.setMessageSource(messageSource);
  }

  @Test
  @DisplayName("계약서 등록")
  void createContract() {
    // given
    given(contractRepository.save(any())).willReturn(contract);

    // when
    ContractDto savedContractDto = contractService.createContract(contractDto, member);

    // then
    verify(contractRepository, times(1)).save(any(Contract.class));

    assertEquals(contractDto.getTitle(), savedContractDto.getTitle());
    assertEquals(contractDto.getContents(), savedContractDto.getContents());
    assertEquals(contractDto.getContractDate(), savedContractDto.getContractDate());
    assertEquals(contractDto.getContractStatus(), savedContractDto.getContractStatus());
    assertEquals(contractDto.getFile(), savedContractDto.getFile());
    assertEquals(contractDto.getMemo(), savedContractDto.getMemo());
  }

  @Test
  @DisplayName("계약서 조회")
  void getContractDetail() {
    // given
    given(contractRepository.findById(contractId)).willReturn(
        Optional.ofNullable(contract));

    // when
    Optional<Contract> result = contractService.findContractById(contractId);

    // then
    verify(contractRepository, times(1)).findById(anyLong());
    assertTrue(result.isPresent());
    assertEquals(contract.getTitle(), result.get().getTitle());
    assertEquals(contract.getContents(), result.get().getContents());
    assertEquals(contract.getContractDate(), result.get().getContractDate());
    assertEquals(contract.getContractStatus(), result.get().getContractStatus());
    assertEquals(contract.getFile(), result.get().getFile());
    assertEquals(contract.getMemo(), result.get().getMemo());
  }

  @Test
  @DisplayName("계약서 리스트 조회")
  void getContractList() {
    // given
    given(contractRepository.findByMemberIdOrderByContractDate(memberId)).willReturn(
        List.of(contract));

    // when
    List<ContractListResponseDto> result = contractService.findContractList(memberId);

    // then
    verify(contractRepository, times(1)).findByMemberIdOrderByContractDate(anyLong());
    assertEquals(1, result.size());
  }

  @Test
  @DisplayName("계약서 내용 수정")
  void modifyContract() {
    // given
    ContractDto requestDto = ContractDto.builder()
        .title("modified_title")
        .contents("modified_contents")
        .contractDate(LocalDate.parse("2022-01-01"))
        .contractStatus(ContractStatus.COMPLETE)
        .memo("modified_memo")
        .build();

    given(contractRepository.findById(contractId)).willReturn(Optional.ofNullable(contract));

    // when
    contractService.modifyContract(contractId, requestDto);

    // then
    Contract modifiedContract = contractRepository.findById(contractId).get();
    assertEquals(requestDto.getTitle(), modifiedContract.getTitle());
    assertEquals(requestDto.getContents(), modifiedContract.getContents());
    assertEquals(requestDto.getContractDate(), modifiedContract.getContractDate());
    assertEquals(requestDto.getContractStatus(), modifiedContract.getContractStatus());
    assertEquals(requestDto.getMemo(), modifiedContract.getMemo());
  }

  @Test
  @DisplayName("계약서 파일 수정")
  void modifyContractFile() {
    // given
    String fileUrl = "modified_file_url";
    given(contractRepository.findById(contractId)).willReturn(Optional.ofNullable(contract));

    // when
    contractService.modifyContractFile(contractId, fileUrl);

    // then
    Contract modifiedContract = contractRepository.findById(contractId).get();
    assertEquals(fileUrl, modifiedContract.getFile());
  }

  @Test
  @DisplayName("존재하지 않는 계약서에 대해 파일 수정 요청을 하면 NotFoundException이 발생한다.")
  void throwNotFoundExceptionByNotExistedContract() {
    assertThrows(NotFoundException.class,
        () -> contractService.modifyContractFile(100L, "test_file_url"));
  }

  @Test
  @DisplayName("존재하지 않는 계약서에 대해 내용 수정 요청을 하면 NotFoundException이 발생한다.")
  void throwNotFoundExceptionByNotExistedContract2() {
    assertThrows(NotFoundException.class,
        () -> contractService.modifyContract(100L, contractDto));
  }

  @Test
  @DisplayName("계약서 삭제")
  void withdrawContract() {
    // given
    given(contractRepository.findById(contractId)).willReturn(
        Optional.ofNullable(contract));

    // when
    boolean result = contractService.withdrawContract(contractId);

    // then
    verify(contractRepository, times(1)).delete(any(Contract.class));
    assertTrue(result);
  }
}
