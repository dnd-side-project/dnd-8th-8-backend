package com.dnd.weddingmap.domain.contract.service.impl;

import com.dnd.weddingmap.domain.contract.Contract;
import com.dnd.weddingmap.domain.contract.dto.ContractDto;
import com.dnd.weddingmap.domain.contract.dto.ContractListResponseDto;
import com.dnd.weddingmap.domain.contract.repository.ContractRepository;
import com.dnd.weddingmap.domain.contract.service.ContractService;
import com.dnd.weddingmap.domain.member.Member;
import com.dnd.weddingmap.global.exception.NotFoundException;
import com.dnd.weddingmap.global.util.MessageUtil;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ContractServiceImpl implements ContractService {

  private final ContractRepository contractRepository;

  @Override
  @Transactional(rollbackFor = Exception.class)
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
  @Transactional(readOnly = true)
  public Optional<Contract> findContractById(Long id) {
    return contractRepository.findById(id);
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public boolean withdrawContract(Long contractId) {
    return contractRepository.findById(contractId)
        .map(contract -> {
          contractRepository.delete(contract);
          return true;
        })
        .orElse(false);
  }

  @Override
  @Transactional(readOnly = true)
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

  @Override
  @Transactional(rollbackFor = Exception.class)
  public ContractDto modifyContractFile(Long contractId, String fileUrl) {
    Contract contract = contractRepository.findById(contractId).orElseThrow(
        () -> new NotFoundException(MessageUtil.getMessage("notFound.contract.exception.msg"))
    );
    return new ContractDto(contract.updateFile(fileUrl));
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public ContractDto modifyContract(Long contractId, ContractDto contractDto) {
    Contract contract = contractRepository.findById(contractId).orElseThrow(
        () -> new NotFoundException(MessageUtil.getMessage("notFound.contract.exception.msg"))
    );
    return new ContractDto(contract.update(contractDto));
  }
}
