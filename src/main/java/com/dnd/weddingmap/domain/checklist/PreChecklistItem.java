package com.dnd.weddingmap.domain.checklist;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Getter;

@Getter
public enum PreChecklistItem {

  MEETING("상견례"),
  WEDDING_HALL("예식장"),
  HONEYMOON("신혼여행"),
  STUDIO("스튜디오"),
  DRESS("드레스"),
  MAKEUP("메이크업"),
  WEDDING_GIFT("예물");

  private final String title;

  PreChecklistItem(String title) {
    this.title = title;
  }

  @JsonCreator
  public static PreChecklistItem findBy(String key) {

    for (PreChecklistItem preChecklistItem : PreChecklistItem.values()) {
      if (preChecklistItem.name().equals(key.toUpperCase())) {
        return preChecklistItem;
      }
    }

    return null;
  }
}
