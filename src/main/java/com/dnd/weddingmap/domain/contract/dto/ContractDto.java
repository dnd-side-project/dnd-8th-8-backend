package com.dnd.weddingmap.domain.contract.dto;

import com.dnd.weddingmap.domain.contract.Contract;
import com.dnd.weddingmap.domain.contract.ContractStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ContractDto {
  private Long id;

  @NotBlank(message = "title은 필수 요청 데이터입니다.")
  private String title;

  @NotBlank(message = "contents는 필수 요청 데이터입니다.")
  private String contents;

  @NotNull(message = "contractDate는 필수 요청 데이터입니다.")
  private LocalDate contractDate;

  @NotNull(message = "contractStatus는 필수 요청 데이터입니다.")
  private ContractStatus contractStatus;

  private String file;

  private String memo;

  @Builder
  public ContractDto(Long id, String title, String contents, LocalDate contractDate,
      ContractStatus contractStatus, String file, String memo) {
    this.id = id;
    this.title = title;
    this.contents = contents;
    this.contractDate = contractDate;
    this.contractStatus = contractStatus;
    this.file = file;
    this.memo = memo;
  }

  public ContractDto(Contract contract) {
    this.id = contract.getId();
    this.title = contract.getTitle();
    this.contents = contract.getContents();
    this.contractDate = contract.getContractDate();
    this.contractStatus = contract.getContractStatus();
    this.file = contract.getFile();
    this.memo = contract.getMemo();
  }
}
