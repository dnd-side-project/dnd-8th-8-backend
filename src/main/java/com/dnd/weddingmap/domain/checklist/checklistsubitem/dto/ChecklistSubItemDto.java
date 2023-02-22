package com.dnd.weddingmap.domain.checklist.checklistsubitem.dto;

import com.dnd.weddingmap.domain.checklist.checklistitem.ChecklistItem;
import com.dnd.weddingmap.domain.checklist.checklistsubitem.ChecklistSubItem;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ChecklistSubItemDto {

  private Long id;
  @NotBlank(message = "contents can't be null")
  private String contents;
  private Boolean isChecked = false;

  public ChecklistSubItemDto(ChecklistSubItem checklistSubItem) {
    this.id = checklistSubItem.getId();
    this.contents = checklistSubItem.getContents();
    this.isChecked = checklistSubItem.getIsChecked();
  }

  public ChecklistSubItem toEntity(ChecklistItem checklistItem) {
    return ChecklistSubItem.builder()
        .id(this.id)
        .contents(this.contents)
        .isChecked(this.isChecked)
        .checklistItem(checklistItem)
        .build();
  }

  @Builder
  public ChecklistSubItemDto(Long id, String contents, Boolean isChecked) {
    this.id = id;
    this.contents = contents;
    this.isChecked = isChecked;
  }
}
