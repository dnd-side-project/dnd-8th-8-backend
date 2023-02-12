package com.dnd.wedding.domain.checklist.checklistsubitem;

import com.dnd.wedding.domain.checklist.checklistitem.ChecklistItem;
import com.dnd.wedding.domain.common.BaseTimeEntity;
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
      String contents, Boolean isChecked, ChecklistItem checklistItem
  ) {
    this.contents = contents;
    this.isChecked = isChecked;
    this.checklistItem = checklistItem;
  }
}
