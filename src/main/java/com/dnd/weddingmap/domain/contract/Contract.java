package com.dnd.weddingmap.domain.contract;

import com.dnd.weddingmap.domain.common.BaseTimeEntity;
import com.dnd.weddingmap.domain.contract.dto.ContractDto;
import com.dnd.weddingmap.domain.member.Member;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.time.LocalDate;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
@Entity
public class Contract extends BaseTimeEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(length = 64, nullable = false)
  private String title;

  @Column
  private String contents;

  @Column(nullable = false)
  private LocalDate contractDate;

  @Column(nullable = false)
  @Enumerated(value = EnumType.STRING)
  private ContractStatus contractStatus;

  @Column
  private String file;

  @Column
  private String memo;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "memberId", nullable = false)
  private Member member;

  @Builder
  public Contract(Long id, String title, String contents, LocalDate contractDate,
      ContractStatus contractStatus, String file, String memo, Member member) {
    this.id = id;
    this.title = title;
    this.contents = contents;
    this.contractDate = contractDate;
    this.contractStatus = contractStatus;
    this.file = file;
    this.memo = memo;
    this.member = member;
  }

  public Contract updateFile(String file) {
    this.file = file;
    return this;
  }

  public Contract update(ContractDto contractDto) {
    this.title = contractDto.getTitle();
    this.contents = contractDto.getContents();
    this.contractDate = contractDto.getContractDate();
    this.contractStatus  = contractDto.getContractStatus();
    this.memo = contractDto.getMemo();

    return this;
  }
}
