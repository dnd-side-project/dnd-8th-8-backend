package com.dnd.wedding.domain.checklist.checklistitem.dto;

import com.dnd.wedding.domain.checklist.checklistitem.ChecklistItem;
import com.dnd.wedding.domain.member.Member;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalTime;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ChecklistItemDto {

  private Long id;
  @NotBlank
  private String title;
  @NotNull
  private LocalDate checkDate;
  private LocalTime time;
  private String place;
  private String memo;

  public ChecklistItemDto(ChecklistItem checklistItem) {
    this.id = checklistItem.getId();
    this.title = checklistItem.getTitle();
    this.checkDate = checklistItem.getCheckDate();
    this.time = checklistItem.getTime();
    this.place = checklistItem.getPlace();
    this.memo = checklistItem.getMemo();
  }

  public ChecklistItem toEntity(Member member) {
    return ChecklistItem.builder()
        .title(this.title)
        .checkDate(this.checkDate)
        .time(this.time)
        .place(this.place)
        .memo(this.memo)
        .member(member)
        .build();
  }
}
