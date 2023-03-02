package com.dnd.weddingmap.domain.contract.service;

import com.dnd.weddingmap.domain.contract.Contract;
import com.dnd.weddingmap.domain.contract.dto.ContractDto;
import com.dnd.weddingmap.domain.contract.dto.ContractListResponseDto;
import com.dnd.weddingmap.domain.member.Member;
import java.util.List;
import java.util.Optional;

public interface ContractService {

  /**
   * 계약서 등록을 진행한다.
   *
   * @param member 계약서를 등록할 회원
   * @param contractDto 계약서 정보를 담은 dto
   * @return 등록된 계약서 정보를 담은 dto
   */
  ContractDto createContract(ContractDto contractDto, Member member);

  /**
   * 계약서를 조회한다.
   *
   * @param id 조회할 계약서 id
   * @return 조회한 계약서
   */
  Optional<Contract> findContractById(Long id);

  /**
   * 계약서를 삭제한다.
   *
   * @param contractId 삭제할 계약서 id
   * @return 계약서 삭제 성공 여부
   */
  boolean withdrawContract(Long contractId);

  /**
   * 회원의 계약서 리스트를 조회한다.
   *
   * @param memberId 회원 id
   * @return 회원의 계약서 리스트 dto
   */
  List<ContractListResponseDto> findContractList(Long memberId);

  /**
   * 계약서 파일을 수정한다.
   *
   * @param contractId 수정할 계약서 id
   * @param fileUrl 수정할 파일 url
   * @return 수정된 계약서 전체 정보가 담긴 dto
   * @Exception NotFoundException 계약서가 존재하지 않는 경우
   */
  ContractDto modifyContractFile(Long contractId, String fileUrl);

  /**
   * 계약서 파일을 제외한 내용을 수정한다.
   *
   * @param contractId 수정할 계약서 id
   * @param contractDto 수정할 계약서 정보를 담은 dto
   * @return 수정된 계약서 전체 정보가 담긴 dto
   * @Exception NotFoundException 계약서가 존재하지 않는 경우
   */
  ContractDto modifyContract(Long contractId, ContractDto contractDto);
}
