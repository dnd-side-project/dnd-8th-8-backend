package com.dnd.weddingmap.domain.contract.service.impl;

import com.dnd.weddingmap.domain.contract.Contract;
import com.dnd.weddingmap.domain.contract.dto.ContractDto;
import com.dnd.weddingmap.domain.contract.dto.ContractListResponseDto;
import com.dnd.weddingmap.domain.contract.repository.ContractRepository;
import com.dnd.weddingmap.domain.contract.service.ContractService;
import com.dnd.weddingmap.domain.member.Member;
import com.dnd.weddingmap.global.exception.ForbiddenException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ContractServiceImpl implements ContractService {

  private final ContractRepository contractRepository;

  @Override
  public ContractDto createContract(ContractDto contractDto, Member member) {
    Contract contract = Contract.builder()
        .title(contractDto.getTitle())
        .contents(contractDto.getContents())
        .contractStatus(contractDto.getContractStatus())
        .contractDate(contractDto.getContractDate())
        .file(contractDto.getFile())
        .memo(contractDto.getMemo())
        .member(member)
        .build();

    return new ContractDto(contractRepository.save(contract));
  }

  @Override
  public Optional<Contract> findContractById(Long id) {
    return contractRepository.findById(id);
  }

  @Override
  public boolean withdrawContract(Long contractId, Long memberId) {
    return contractRepository.findById(contractId)
        .map(contract -> {
          if (!Objects.equals(contract.getMember().getId(), memberId)) {
            throw new ForbiddenException("접근할 수 없는 계약서입니다.");
          }
          contractRepository.delete(contract);
          return true;
        })
        .orElse(false);
  }

  @Override
  public List<ContractListResponseDto> findContractList(Long memberId) {
    List<ContractListResponseDto> result = new ArrayList<>();

    contractRepository.findByMemberIdOrderByContractDate(memberId).forEach(
        contract -> result.add(ContractListResponseDto.builder()
            .id(contract.getId())
            .title(contract.getTitle())
            .contractDate(contract.getContractDate())
            .contractStatus(contract.getContractStatus())
            .build()));

    return result;
  }
}
