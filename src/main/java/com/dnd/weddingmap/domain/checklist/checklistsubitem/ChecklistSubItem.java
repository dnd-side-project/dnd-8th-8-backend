package com.dnd.weddingmap.domain.checklist.checklistsubitem;

import com.dnd.weddingmap.domain.checklist.checklistitem.ChecklistItem;
import com.dnd.weddingmap.domain.checklist.checklistsubitem.dto.ChecklistSubItemDto;
import com.dnd.weddingmap.domain.checklist.checklistsubitem.dto.ChecklistSubItemStateDto;
import com.dnd.weddingmap.domain.common.BaseTimeEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.DynamicInsert;

@DynamicInsert
@Getter
@RequiredArgsConstructor
@Entity
public class ChecklistSubItem extends BaseTimeEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  @Column(length = 64, nullable = false)
  private String contents;
  @Column(nullable = false)
  private Boolean isChecked;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "checklistItemId", nullable = false)
  private ChecklistItem checklistItem;

  @Builder
  public ChecklistSubItem(
      Long id, String contents, Boolean isChecked, ChecklistItem checklistItem
  ) {
    this.id = id;
    this.contents = contents;
    this.isChecked = isChecked;
    this.checklistItem = checklistItem;
  }

  public ChecklistSubItem update(ChecklistSubItemDto checklistSubItemDto) {
    this.contents = checklistSubItemDto.getContents();
    this.isChecked = checklistSubItemDto.getIsChecked();
    return this;
  }

  public ChecklistSubItem updateState(ChecklistSubItemStateDto checklistSubItemStateDto) {
    this.isChecked = checklistSubItemStateDto.getIsChecked();
    return this;
  }
}
