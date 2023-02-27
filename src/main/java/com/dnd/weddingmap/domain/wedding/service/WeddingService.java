package com.dnd.weddingmap.domain.wedding.service;

import com.dnd.weddingmap.domain.member.Member;
import com.dnd.weddingmap.domain.wedding.dto.TotalBudgetDto;
import com.dnd.weddingmap.domain.wedding.dto.WeddingDayDto;

public interface WeddingService {

  /**
   * 결혼정보 등록을 진행한다.
   *
   * @param member 결혼정보를 등록할 회원
   * @param weddingDayDto 결혼식 날짜 정보를 담은 DTO
   * @return 등록된 결혼정보 ID
   * @Exception IllegalStateException 결혼정보가 이미 존재하는 경우 발생
   */
  Long registerWedding(Member member, WeddingDayDto weddingDayDto);


  /**
   * 회원의 결혼식 날짜를 수정한다.
   *
   * @param member 결혼식 날짜를 수정할 회원
   * @param weddingDayDto 결혼식 날짜 정보를 담은 DTO
   * @Exception IllegalStateException 결혼정보가 존재하지 않는 경우 발생
   */
  void modifyWeddingDay(Member member, WeddingDayDto weddingDayDto);

  /**
   * 회원의 결혼식 날짜를 조회한다.
   *
   * @param member 결혼식 날짜를 조회할 회원
   * @return 회원의 결혼식 날짜 정보를 담은 DTO
   * @Exception IllegalStateException 결혼정보가 존재하지 않는 경우 발생
   */
  WeddingDayDto getWeddingDay(Member member);

  /**
   * 회원의 총예산을 수정한다.
   *
   * @param member 총예산을 수정할 회원
   * @param totalBudgetDto 총예산 정보를 담은 DTO
   * @Exception IllegalStateException 결혼정보가 존재하지 않는 경우 발생
   */
  void modifyTotalBudget(Member member, TotalBudgetDto totalBudgetDto);

  /**
   * 회원의 총예산을 조회한다.
   *
   * @param member 총예산을 조회할 회원
   * @return 회원의 총예산 정보를 담은 DTO
   * @Exception IllegalStateException 결혼정보가 존재하지 않는 경우 발생
   */
  TotalBudgetDto getTotalBudget(Member member);
}
