package com.dnd.weddingmap.domain.checklist.checklistitem;

import com.dnd.weddingmap.domain.checklist.checklistitem.dto.ChecklistItemDto;
import com.dnd.weddingmap.domain.common.BaseTimeEntity;
import com.dnd.weddingmap.domain.member.Member;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.time.LocalDate;
import java.time.LocalTime;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@Entity
public class ChecklistItem extends BaseTimeEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  @Column(length = 64, nullable = false)
  private String title;
  @Column
  private LocalDate checkDate;
  @Column
  private LocalTime startTime;
  @Column
  private LocalTime endTime;
  @Column
  private String place;
  @Column
  private String memo;
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "memberId", nullable = false)
  private Member member;

  @Builder
  public ChecklistItem(
      Long id, String title, LocalDate checkDate, LocalTime startTime, LocalTime endTime,
      String place, String memo,
      Member member
  ) {
    this.id = id;
    this.title = title;
    this.checkDate = checkDate;
    this.startTime = startTime;
    this.endTime = endTime;
    this.place = place;
    this.memo = memo;
    this.member = member;
  }

  public ChecklistItem update(ChecklistItemDto checklistItemDto) {
    this.title = checklistItemDto.getTitle();
    this.checkDate = checklistItemDto.getCheckDate();
    this.startTime = checklistItemDto.getStartTime();
    this.endTime = checklistItemDto.getEndTime();
    this.place = checklistItemDto.getPlace();
    this.memo = checklistItemDto.getMemo();

    return this;
  }
}
