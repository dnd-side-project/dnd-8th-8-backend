package com.dnd.weddingmap.domain.wedding.service;

import com.dnd.weddingmap.domain.wedding.dto.TotalBudgetDto;
import com.dnd.weddingmap.domain.wedding.dto.WeddingDayDto;

public interface WeddingService {

  /**
   * 결혼정보 등록을 진행한다.
   *
   * @param memberId 결혼정보를 등록할 회원 ID
   * @param weddingDayDto 결혼식 날짜 정보를 담은 DTO
   * @return 등록된 결혼정  ID
   * @Exception IllegalStateException 결혼정보가 이미 존재하는 경우 발생
   */
  Long registerWedding(Long memberId, WeddingDayDto weddingDayDto);


  /**
   * 회원의 결혼식 날짜를 수정한다.
   *
   * @param member 결혼식 날짜를 수정할 회원 ID
   * @param weddingDayDto 결혼식 날짜 정보를 담은 DTO
   * @Exception IllegalStateException 결혼정보가 존재하지 않는 경우 발생
   */
  void modifyWeddingDay(Long memberId, WeddingDayDto weddingDayDto);

  /**
   * 회원의 결혼식 날짜를 조회한다.
   *
   * @param member 결혼식 날짜를 조회할 회원 ID
   * @return 회원의 결혼식 날짜 정보를 담은 DTO
   * @Exception IllegalStateException 결혼정보가 존재하지 않는 경우 발생
   */
  WeddingDayDto getWeddingDay(Long memberId);

  /**
   * 회원의 총예산을 수정한다.
   *
   * @param member 총예산을 수정할 회원 ID
   * @param totalBudgetDto 총예산 정보를 담은 DTO
   * @Exception IllegalStateException 결혼정보가 존재하지 않는 경우 발생
   */
  void modifyTotalBudget(Long memberId, TotalBudgetDto totalBudgetDto);

  /**
   * 회원의 총예산을 조회한다.
   *
   * @param member 총예산을 조회할 회원 ID
   * @return 회원의 총예산 정보를 담은 DTO
   * @Exception IllegalStateException 결혼정보가 존재하지 않는 경우 발생
   */
  TotalBudgetDto getTotalBudget(Long memberId);
}
