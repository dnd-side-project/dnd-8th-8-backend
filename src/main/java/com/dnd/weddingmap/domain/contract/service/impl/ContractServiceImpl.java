package com.dnd.weddingmap.domain.contract.service.impl;

import com.dnd.weddingmap.domain.contract.Contract;
import com.dnd.weddingmap.domain.contract.dto.ContractDto;
import com.dnd.weddingmap.domain.contract.repository.ContractRepository;
import com.dnd.weddingmap.domain.contract.service.ContractService;
import com.dnd.weddingmap.domain.member.Member;
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
}
