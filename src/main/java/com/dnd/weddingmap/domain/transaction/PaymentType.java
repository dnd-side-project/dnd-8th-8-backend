package com.dnd.weddingmap.domain.transaction;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PaymentType {

  CASH("현금"),
  CARD("카드");

  private final String title;

  @JsonCreator
  public static PaymentType findBy(String key) {

    for (PaymentType paymentType :
        PaymentType.values()) {
      if (paymentType.name().equals(key.toUpperCase())) {
        return paymentType;
      }
    }

    return null;
  }
}
