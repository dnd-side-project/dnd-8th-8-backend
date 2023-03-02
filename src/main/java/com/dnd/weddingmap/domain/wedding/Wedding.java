package com.dnd.weddingmap.domain.wedding;

import com.dnd.weddingmap.domain.common.BaseTimeEntity;
import com.dnd.weddingmap.domain.member.Member;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
public class Wedding extends BaseTimeEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @OneToMany(mappedBy = "wedding")
  private List<Member> weddingMembers = new ArrayList<>();

  @Column(nullable = false)
  private LocalDate weddingDay;

  @Column(nullable = false)
  private boolean preparing;

  @Column(nullable = false)
  private Long budget;
  
  public Wedding(Member member, LocalDate weddingDay, boolean preparing) {
    this.weddingMembers.add(member);
    this.weddingDay = weddingDay;
    this.preparing = preparing;
    this.budget = 0L;
  }

  @Builder
  public Wedding(Long id, Member member, LocalDate weddingDay, boolean preparing, Long budget) {
    this.id = id;
    this.weddingMembers.add(member);
    this.weddingDay = weddingDay;
    this.preparing = preparing;
    this.budget = budget;
  }

  public void removeMember(Member member) {
    this.weddingMembers.remove(member);
  }

  public void setWeddingDay(LocalDate weddingDay) {
    this.weddingDay = weddingDay;
  }

  public void setBudget(Long budget) {
    this.budget = budget;
  }

  public void setPreparing(boolean preparing) {
    this.preparing = preparing;
  }
}
