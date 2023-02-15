package com.dnd.wedding.domain.checklist.checklistitem;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import com.dnd.wedding.domain.checklist.checklistitem.dto.ChecklistItemDto;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Month;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ChecklistItemTest {

  private final ChecklistItem checklistItem = ChecklistItem.builder()
      .title("title")
      .checkDate(LocalDate.of(2023, Month.OCTOBER, 1))
      .time(LocalTime.MAX)
      .place("place")
      .memo("memo")
      .build();

  @Test
  @DisplayName("체크리스트 아이템 아이디 조회")
  void getId() {
    assertNull(checklistItem.getId());
  }

  @Test
  @DisplayName("체크리스트 아이템 제목 조회")
  void getTitle() {
    assertEquals("title", checklistItem.getTitle());
  }

  @Test
  @DisplayName("체크리스트 아이템 일시 조회")
  void getCheckDate() {
    assertEquals(LocalDate.of(2023, Month.OCTOBER, 1), checklistItem.getCheckDate());
  }

  @Test
  @DisplayName("체크리스트 아이템 일정 시간 조회")
  void getTime() {
    assertEquals(LocalTime.MAX, checklistItem.getTime());
  }

  @Test
  @DisplayName("체크리스트 아이템 장소 조회")
  void getPlace() {
    assertEquals("place", checklistItem.getPlace());
  }

  @Test
  @DisplayName("체크리스트 아이템 메모 조회")
  void getMemo() {
    assertEquals("memo", checklistItem.getMemo());
  }

  @Test
  @DisplayName("BaseTimeEntity 생성 시간 조회")
  void getCreatedAt() {
    assertNull(checklistItem.getCreatedAt());
  }

  @Test
  @DisplayName("BaseTimeEntity 수정 시간 조회")
  void getModifiedAt() {
    assertNull(checklistItem.getModifiedAt());
  }

  @Test
  @DisplayName("체크리스트 아이템 정보 수정")
  void update() {
    String title = "test title";
    LocalDate checkDate = LocalDate.of(2020, Month.OCTOBER, 1);
    LocalTime time = LocalTime.MIN;
    String place = "test place";
    String memo = "test memo";

    checklistItem.update(ChecklistItemDto.builder()
        .title(title)
        .checkDate(checkDate)
        .time(time)
        .place(place)
        .memo(memo)
        .build()
    );

    assertEquals(title, checklistItem.getTitle());
    assertEquals(checkDate, checklistItem.getCheckDate());
    assertEquals(title, checklistItem.getTitle());
    assertEquals(place, checklistItem.getPlace());
    assertEquals(memo, checklistItem.getMemo());
  }
}
