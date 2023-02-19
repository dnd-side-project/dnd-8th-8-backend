package com.dnd.weddingmap.domain.checklist.checklistitem.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.dnd.weddingmap.domain.checklist.checklistitem.ChecklistItem;
import com.dnd.weddingmap.domain.member.Member;
import com.dnd.weddingmap.domain.member.MemberRepository;
import com.dnd.weddingmap.domain.member.Role;
import com.dnd.weddingmap.domain.oauth.OAuth2Provider;
import com.dnd.weddingmap.global.config.JpaConfig;
import jakarta.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Month;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;

@Transactional
@DataJpaTest(includeFilters = @ComponentScan.Filter(
    type = FilterType.ASSIGNABLE_TYPE, classes = JpaConfig.class))
class ChecklistItemRepositoryTest {

  @Autowired
  ChecklistItemRepository checklistItemRepository;

  @Autowired
  MemberRepository memberRepository;

  Member member;

  @BeforeEach
  void init() {
    member = memberRepository.save(Member.builder()
        .name("test1")
        .email("test1@example.com")
        .profileImage("test1.png")
        .role(Role.USER)
        .oauth2Provider(OAuth2Provider.GOOGLE)
        .build());
  }

  @Test
  @DisplayName("사용자 아이디로 체크리스트 아이템 조회")
  void findByMemberId() {
    ChecklistItem checklistItem1 = ChecklistItem.builder()
        .title("test title1")
        .checkDate(LocalDate.of(2023, Month.OCTOBER, 1))
        .startTime(LocalTime.MIN)
        .endTime(LocalTime.MAX)
        .place("test place1")
        .memo("test memo1")
        .member(member)
        .build();

    ChecklistItem checklistItem2 = ChecklistItem.builder()
        .title("test title2")
        .checkDate(LocalDate.of(2023, Month.OCTOBER, 2))
        .startTime(LocalTime.MIN)
        .endTime(LocalTime.MAX)
        .place("test place2")
        .memo("test memo2")
        .member(member)
        .build();

    checklistItemRepository.save(checklistItem1);
    checklistItemRepository.save(checklistItem2);

    List<ChecklistItem> checklistItems = checklistItemRepository.findByMemberId(member.getId());

    assertEquals(2, checklistItems.size());
  }

  @Test
  @DisplayName("아이디로 체크리스트 아이템 조회")
  void findById() {
    String title = "test title";
    LocalDate checkDate = LocalDate.of(2023, Month.OCTOBER, 1);
    LocalTime startTime = LocalTime.MIN;
    LocalTime endTime = LocalTime.MAX;
    String place = "test place";
    String memo = "test memo";

    ChecklistItem savedChecklistItem = checklistItemRepository.save(ChecklistItem.builder()
        .title(title)
        .checkDate(checkDate)
        .startTime(startTime)
        .endTime(endTime)
        .place(place)
        .memo(memo)
        .member(member)
        .build());

    Optional<ChecklistItem> checklistItem = checklistItemRepository.findById(
        savedChecklistItem.getId());

    assertTrue(checklistItem.isPresent());
    assertEquals(title, checklistItem.get().getTitle());
    assertEquals(checkDate, checklistItem.get().getCheckDate());
    assertEquals(startTime, checklistItem.get().getStartTime());
    assertEquals(endTime, checklistItem.get().getEndTime());
    assertEquals(place, checklistItem.get().getPlace());
    assertEquals(memo, checklistItem.get().getMemo());
  }
}


