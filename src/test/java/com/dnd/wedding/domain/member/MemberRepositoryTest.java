package com.dnd.wedding.domain.member;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.dnd.wedding.domain.oauth.OAuth2Provider;
import com.dnd.wedding.global.config.JpaConfig;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;

@DataJpaTest(includeFilters = @ComponentScan.Filter(
    type = FilterType.ASSIGNABLE_TYPE, classes = JpaConfig.class))
class MemberRepositoryTest {

  @Autowired
  MemberRepository memberRepository;

  @BeforeEach
  void save() {
    Member member1 = Member.builder().name("test1").email("test1@example.com")
        .profileImage("test1.png").role(Role.USER).oauth2Provider(OAuth2Provider.GOOGLE).build();

    Member member2 = Member.builder().name("test2").email("test2@example.com")
        .profileImage("test2.png").role(Role.USER).oauth2Provider(OAuth2Provider.GOOGLE).build();

    memberRepository.save(member1);
    memberRepository.save(member2);

  }

  @Test
  @DisplayName("총 회원 수 조회")
  void getMembers() {
    // when
    List<Member> members = memberRepository.findAll();

    // then
    assertEquals(2, members.size());
  }

  @Test
  @DisplayName("이메일로 회원 조회")
  void findByEmail() {
    // given
    String name = "test1";
    String email = "test1@example.com";

    // when
    Optional<Member> member = memberRepository.findByEmail(email);

    // then
    assertTrue(member.isPresent());
    assertEquals(name, member.get().getName());
  }

  @Test
  @DisplayName("타임스탬프가 업데이트 되는지 확인")
  void checkTimeStamp() {
    String email = "test1@example.com";

    Optional<Member> member = memberRepository.findByEmail(email);

    assertTrue(member.isPresent());
    assertEquals(member.get().getCreatedAt(), member.get().getModifiedAt());

    memberRepository.save(member.get().update("new name", "new image.png"));

    Optional<Member> updatedMember = memberRepository.findByEmail(email);
    assertTrue(updatedMember.isPresent());
    assertTrue(updatedMember.get().getCreatedAt().isBefore(updatedMember.get().getModifiedAt()));
  }
}
