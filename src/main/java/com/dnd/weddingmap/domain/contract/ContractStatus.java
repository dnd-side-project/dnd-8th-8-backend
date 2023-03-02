package com.dnd.weddingmap.domain.contract;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ContractStatus {

  COMPLETE("계약 완료"),
  IN_PROGRESS("계약중"),
  VERBAL("구두 계약"),
  PROVISIONAL("가계약");

  private final String title;

  @JsonCreator
  public static ContractStatus findBy(String key) {

    for (ContractStatus contractStatus :
        ContractStatus.values()) {
      if (contractStatus.name().equals(key.toUpperCase())) {
        return contractStatus;
      }
    }

    return null;
  }
}
