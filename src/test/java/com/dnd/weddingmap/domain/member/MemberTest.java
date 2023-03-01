package com.dnd.weddingmap.domain.member;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import com.dnd.weddingmap.domain.oauth.OAuth2Provider;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class MemberTest {

  private final Member member = Member.builder()
      .name("test")
      .email("test@example.com")
      .profileImage("test.png")
      .role(Role.USER)
      .oauth2Provider(OAuth2Provider.GOOGLE)
      .build();

  @Test
  @DisplayName("회원 아이디 조회")
  void getId() {
    assertNull(member.getId());
  }

  @Test
  @DisplayName("회원 이름 조회")
  void getName() {
    assertEquals("test", member.getName());
  }

  @Test
  @DisplayName("회원 이메일 조회")
  void getEmail() {
    assertEquals("test@example.com", member.getEmail());
  }

  @Test
  @DisplayName("회원 프로필 이미지 조회")
  void getProfileImage() {
    assertEquals("test.png", member.getProfileImage());
  }

  @Test
  @DisplayName("회원 권한 조회")
  void getRole() {
    assertEquals(Role.USER, member.getRole());
  }

  @Test
  @DisplayName("회원 OAuth2 Provider 조회")
  void getOauth2Provider() {
    assertEquals(OAuth2Provider.GOOGLE, member.getOauth2Provider());
  }

  @Test
  @DisplayName("BaseTimeEntity 생성 시간 조회")
  void getCreatedAt() {
    assertNull(member.getCreatedAt());
  }

  @Test
  @DisplayName("BaseTimeEntity 수정 시간 조회")
  void getModifiedAt() {
    assertNull(member.getModifiedAt());
  }
}
