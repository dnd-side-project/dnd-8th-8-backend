package com.dnd.weddingmap.domain.transaction;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum TransactionCategory {

  CASH("현금"),
  CARD("카드");

  private final String title;

  @JsonCreator
  public static com.dnd.weddingmap.domain.transaction.TransactionCategory findBy(String key) {

    for (com.dnd.weddingmap.domain.transaction.TransactionCategory transactionCategory : com.dnd.weddingmap.domain.transaction.TransactionCategory.values()) {
      if (transactionCategory.name().equals(key.toUpperCase())) {
        return transactionCategory;
      }
    }

    return null;
  }
}
