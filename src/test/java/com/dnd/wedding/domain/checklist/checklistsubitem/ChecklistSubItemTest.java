package com.dnd.wedding.domain.checklist.checklistsubitem;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import com.dnd.wedding.domain.checklist.checklistsubitem.dto.ChecklistSubItemDto;
import com.dnd.wedding.domain.checklist.checklistsubitem.dto.UpdateChecklistSubItemDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ChecklistSubItemTest {

  private final ChecklistSubItem checklistSubItem = ChecklistSubItem.builder()
      .contents("contents")
      .isChecked(false)
      .build();

  @Test
  @DisplayName("체크리스트 서브 아이템 아이디 조회")
  void getId() {
    assertNull(checklistSubItem.getId());
  }

  @Test
  @DisplayName("체크리스트 서브 아이템 내용 조회")
  void getContents() {
    assertEquals("contents", checklistSubItem.getContents());
  }

  @Test
  @DisplayName("체크리스트 서브 아이템 체크 여부 조회")
  void getCheckDate() {
    assertEquals(false, checklistSubItem.getIsChecked());
  }

  @Test
  @DisplayName("BaseTimeEntity 생성 시간 조회")
  void getCreatedAt() {
    assertNull(checklistSubItem.getCreatedAt());
  }

  @Test
  @DisplayName("BaseTimeEntity 수정 시간 조회")
  void getModifiedAt() {
    assertNull(checklistSubItem.getModifiedAt());
  }

  @Test
  @DisplayName("체크리스트 서브 아이템 정보 수정")
  void update() {
    String contents = "test contents";
    Boolean isChecked = true;

    checklistSubItem.update(ChecklistSubItemDto.builder()
        .contents(contents)
        .isChecked(isChecked)
        .build()
    );

    assertEquals(contents, checklistSubItem.getContents());
    assertEquals(isChecked, checklistSubItem.getIsChecked());
  }

  @Test
  @DisplayName("체크리스트 서브 아이템 체크 여부 수정")
  void updateState() {
    Boolean isChecked = true;

    checklistSubItem.updateState(UpdateChecklistSubItemDto.builder()
        .isChecked(isChecked)
        .build()
    );

    assertEquals(isChecked, checklistSubItem.getIsChecked());
  }
}
