package com.dnd.weddingmap.domain.contract.dto;

import com.dnd.weddingmap.domain.contract.ContractStatus;
import java.time.LocalDate;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ContractListResponseDto {

  private Long id;
  private String title;
  private LocalDate contractDate;
  private ContractStatus contractStatus;

  @Builder
  public ContractListResponseDto(Long id, String title, LocalDate contractDate,
      ContractStatus contractStatus) {
    this.id = id;
    this.title = title;
    this.contractDate = contractDate;
    this.contractStatus = contractStatus;
  }
}
