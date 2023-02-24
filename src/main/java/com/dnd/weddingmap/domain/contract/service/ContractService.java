package com.dnd.weddingmap.domain.contract.service;

import com.dnd.weddingmap.domain.contract.Contract;
import com.dnd.weddingmap.domain.contract.dto.ContractDto;
import com.dnd.weddingmap.domain.member.Member;
import java.util.Optional;

public interface ContractService {

  ContractDto createContract(ContractDto contractDto, Member member);
  Optional<Contract> findContractById(Long id);
}
