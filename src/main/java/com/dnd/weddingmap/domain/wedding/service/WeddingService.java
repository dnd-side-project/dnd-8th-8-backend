package com.dnd.weddingmap.domain.wedding.service;

import com.dnd.weddingmap.domain.member.Member;
import com.dnd.weddingmap.domain.wedding.dto.WeddingDayDto;

public interface WeddingService {

  /**
   * 결혼식 등록을 진행한다. 만약 회원의 결혼식이 이미 등록되어 있다면 예외를 발생시킨다.
   * @param member 결혼식을 등록할 회원
   * @param weddingDayDto 결혼식 날짜 정보를 담은 DTO
   */
  Long registerWedding(Member member, WeddingDayDto weddingDayDto);

}
