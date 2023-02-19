package com.dnd.wedding.domain.member;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum Gender {

  MALE,
  FEMALE;

  @JsonCreator
  public static Gender findBy(String value) {

    for (Gender gender : Gender.values()) {
      if (gender.name().equals(value.toUpperCase())) {
        return gender;
      }
    }

    return null;
  }
}
