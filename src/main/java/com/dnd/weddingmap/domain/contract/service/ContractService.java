package com.dnd.weddingmap.domain.contract.service;

import com.dnd.weddingmap.domain.contract.Contract;
import com.dnd.weddingmap.domain.contract.dto.ContractDto;
import com.dnd.weddingmap.domain.contract.dto.ContractListResponseDto;
import com.dnd.weddingmap.domain.member.Member;
import java.util.List;
import java.util.Optional;

public interface ContractService {

  ContractDto createContract(ContractDto contractDto, Member member);

  Optional<Contract> findContractById(Long id);

  boolean withdrawContract(Long contractId, Long memberId);

  List<ContractListResponseDto> findContractList(Long memberId);

  ContractDto modifyContractFile(Long contractId, String fileUrl);

  ContractDto modifyContract(Long contractId, ContractDto contractDto);
}
