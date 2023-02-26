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
import java.util.List;
import lombok.Getter;

@Getter
@Entity
public class Wedding extends BaseTimeEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @OneToMany(mappedBy = "wedding")
  private List<Member> weddingMembers;

  @Column(nullable = false)
  private LocalDate weddingDay;

  @Column(nullable = false)
  private Long totalBudget;

  public Wedding() {
    this.weddingDay = LocalDate.now();
    this.totalBudget = 0L;
  }
  
  public Wedding(Member member, LocalDate weddingDay) {
    this.weddingMembers.add(member);
    this.weddingDay = weddingDay;
    this.totalBudget = 0L;
  }
}
