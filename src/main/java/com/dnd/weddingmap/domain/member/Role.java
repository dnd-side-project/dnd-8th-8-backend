package com.dnd.weddingmap.domain.member;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Role {

  USER("ROLE_USER");

  private final String key;
}
