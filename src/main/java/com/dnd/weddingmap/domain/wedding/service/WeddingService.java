package com.dnd.weddingmap.domain.wedding.service;

import com.dnd.weddingmap.domain.wedding.dto.BudgetDto;
import com.dnd.weddingmap.domain.wedding.dto.WeddingDayDto;

public interface WeddingService {

  /**
   * 결혼 등록을 진행한다.
   *
   * @param memberId 결혼을 등록할 회원 ID
   * @param weddingDayDto 결혼일 정보를 담은 DTO
   * @return 등록된 결혼 ID
   * @Exception IllegalStateException 결혼이 이미 존재하는 경우 발생
   */
  Long registerWedding(Long memberId, WeddingDayDto weddingDayDto);


  /**
   * 회원의 결혼일을 수정한다.
   *
   * @param memberId 결혼일을 수정할 회원 ID
   * @param weddingDayDto 결혼일 정보를 담은 DTO
   * @Exception IllegalStateException 결혼이 존재하지 않는 경우 발생
   */
  void modifyWeddingDay(Long memberId, WeddingDayDto weddingDayDto);

  /**
   * 회원의 결혼일을 조회한다.
   *
   * @param memberId 결혼일을 조회할 회원 ID
   * @return 회원의 결혼일 정보를 담은 DTO
   * @Exception IllegalStateException 결혼이 존재하지 않는 경우 발생
   */
  WeddingDayDto getWeddingDay(Long memberId);

  /**
   * 회원의 결혼 예산을 수정한다.
   *
   * @param memberId 결혼 예산을 수정할 회원 ID
   * @param budgetDto 결혼 예산 정보를 담은 DTO
   * @Exception IllegalStateException 결혼이 존재하지 않는 경우 발생
   */
  void modifyBudget(Long memberId, BudgetDto budgetDto);

  /**
   * 회원의 결혼 예산을 조회한다.
   *
   * @param memberId 결혼 예산을 조회할 회원 ID
   * @return 회원의 결혼 예산 정보를 담은 DTO
   * @Exception IllegalStateException 결혼이 존재하지 않는 경우 발생
   */
  BudgetDto getBudget(Long memberId);
}
