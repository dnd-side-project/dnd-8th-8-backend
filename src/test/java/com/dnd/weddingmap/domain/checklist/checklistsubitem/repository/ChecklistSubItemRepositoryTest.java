package com.dnd.weddingmap.domain.checklist.checklistsubitem.repository;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.dnd.weddingmap.domain.checklist.checklistitem.ChecklistItem;
import com.dnd.weddingmap.domain.checklist.checklistitem.repository.ChecklistItemRepository;
import com.dnd.weddingmap.domain.checklist.checklistsubitem.ChecklistSubItem;
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
class ChecklistSubItemRepositoryTest {

  @Autowired
  ChecklistItemRepository checklistItemRepository;

  @Autowired
  ChecklistSubItemRepository checklistSubItemRepository;

  @Autowired
  MemberRepository memberRepository;

  ChecklistItem checklistItem;
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

    checklistItem = checklistItemRepository.save(ChecklistItem.builder()
        .title("title")
        .checkDate(LocalDate.of(2023, Month.OCTOBER, 1))
        .startTime(LocalTime.MIN)
        .endTime(LocalTime.MAX)
        .place("place")
        .memo("memo")
        .member(member)
        .build());
  }

  @Test
  @DisplayName("체크리스트 아이템 아이디로 체크리스트 서브 아이템 조회")
  void findAllByChecklistItemId() {
    ChecklistSubItem checklistSubItem1 = ChecklistSubItem.builder()
        .contents("content1")
        .isChecked(false)
        .checklistItem(checklistItem)
        .build();

    ChecklistSubItem checklistSubItem2 = ChecklistSubItem.builder()
        .contents("content2")
        .isChecked(false)
        .checklistItem(checklistItem)
        .build();

    checklistSubItemRepository.save(checklistSubItem1);
    checklistSubItemRepository.save(checklistSubItem2);

    List<ChecklistSubItem> checklistSubItems = checklistSubItemRepository.findAllByChecklistItemId(
        checklistItem.getId());

    assertEquals(2, checklistSubItems.size());
  }

  @Test
  @DisplayName("아이디로 체크리스트 서브 아이템 조회")
  void findById() {
    String contents = "test contenst";
    Boolean isChecked = false;

    ChecklistSubItem savedChecklistSubItem = checklistSubItemRepository.save(
        ChecklistSubItem.builder()
            .contents(contents)
            .isChecked(isChecked)
            .checklistItem(checklistItem)
            .build());

    Optional<ChecklistSubItem> checklistSubItem = checklistSubItemRepository.findById(
        savedChecklistSubItem.getId());

    assertTrue(checklistSubItem.isPresent());
    assertEquals(contents, checklistSubItem.get().getContents());
    assertEquals(isChecked, checklistSubItem.get().getIsChecked());
  }
}
