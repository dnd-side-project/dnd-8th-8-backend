package com.dnd.wedding.domain.checklist.checklistitem.dto;

import com.dnd.wedding.domain.checklist.checklistitem.ChecklistItem;
import com.dnd.wedding.domain.member.Member;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDate;
import java.time.LocalTime;
import lombok.Builder;
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
  private LocalDate checkDate;
  private LocalTime startTime;
  private LocalTime endTime;
  private String place;
  private String memo;

  public ChecklistItemDto(ChecklistItem checklistItem) {
    this.id = checklistItem.getId();
    this.title = checklistItem.getTitle();
    this.checkDate = checklistItem.getCheckDate();
    this.startTime = checklistItem.getStartTime();
    this.endTime = checklistItem.getEndTime();
    this.place = checklistItem.getPlace();
    this.memo = checklistItem.getMemo();
  }

  public ChecklistItem toEntity(Member member) {
    return ChecklistItem.builder()
        .id(this.id)
        .title(this.title)
        .checkDate(this.checkDate)
        .startTime(this.startTime)
        .endTime(this.endTime)
        .place(this.place)
        .memo(this.memo)
        .member(member)
        .build();
  }

  @Builder
  public ChecklistItemDto(Long id, String title, LocalDate checkDate, LocalTime startTime,
      LocalTime endTime, String place,
      String memo) {
    this.id = id;
    this.title = title;
    this.checkDate = checkDate;
    this.startTime = startTime;
    this.endTime = endTime;
    this.place = place;
    this.memo = memo;
  }
}
