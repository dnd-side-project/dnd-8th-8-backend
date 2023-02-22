package com.dnd.weddingmap.domain.checklist.dto;

import com.dnd.weddingmap.domain.checklist.PreChecklistItem;
import com.dnd.weddingmap.global.validator.ValidEnum;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PreChecklistItemListDto {

  @NotNull
  private List<@ValidEnum(
      value = PreChecklistItem.class, message = "preChecklistItem 항목을 다시 확인해주세요.")
      PreChecklistItem> preChecklistItems;

  @Builder
  public PreChecklistItemListDto(List<PreChecklistItem> preChecklistItems) {
    this.preChecklistItems = preChecklistItems;
  }
}
