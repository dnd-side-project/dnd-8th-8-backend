package com.dnd.wedding.domain.checklist.checklistitem;

import com.dnd.wedding.domain.common.BaseTimeEntity;
import com.dnd.wedding.domain.member.Member;
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
  @Column(nullable = false)
  private LocalDate checkDate;
  @Column
  private LocalTime time;
  @Column
  private String place;
  @Column
  private String memo;
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "memberId", nullable = false)
  private Member member;

  @Builder
  public ChecklistItem(
      String title, LocalDate checkDate, LocalTime time, String place, String memo, Member member
  ) {
    this.title = title;
    this.checkDate = checkDate;
    this.time = time;
    this.place = place;
    this.memo = memo;
    this.member = member;
  }
}
