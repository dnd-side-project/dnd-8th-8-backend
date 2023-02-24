package com.dnd.weddingmap.domain.contract.service;

import com.dnd.weddingmap.domain.contract.dto.ContractDto;
import com.dnd.weddingmap.domain.member.Member;

public interface ContractService {

  ContractDto createContract(ContractDto contractDto, Member member);
}
