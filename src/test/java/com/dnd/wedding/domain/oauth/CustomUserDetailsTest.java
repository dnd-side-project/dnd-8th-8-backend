package com.dnd.wedding.domain.oauth;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mockStatic;

import com.dnd.wedding.domain.member.Member;
import com.dnd.wedding.domain.member.Role;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.context.event.annotation.AfterTestClass;
import org.springframework.test.context.event.annotation.BeforeTestClass;

class CustomUserDetailsTest {

  private static final Collection<? extends GrantedAuthority> authority = Collections.singletonList(
      new SimpleGrantedAuthority("ROLE_USER"));

  private static MockedStatic<CustomUserDetails> customUserDetails;
  CustomUserDetails customUserDetailsObject = new CustomUserDetails(1L, "test@test.com",
      OAuth2Provider.GOOGLE, Role.USER, authority);

  Member member = Member.builder()
      .name("test")
      .email("test@test.com")
      .oauth2Provider(OAuth2Provider.GOOGLE)
      .build();

  @BeforeTestClass
  public void beforeTestClass() {
    customUserDetails = mockStatic(CustomUserDetails.class);
  }

  @AfterTestClass
  public void afterTestClass() {
    customUserDetails.close();
  }


  @Test
  @DisplayName("Member 객체를 통한 CustomUserDetails 생성")
  void createCustomUserDetailsByMember() {
    CustomUserDetails customUserDetails1 = CustomUserDetails.create(member);

    assertNotNull(customUserDetails1);
    assertEquals(member.getId(), customUserDetails1.getId());
    assertEquals(member.getEmail(), customUserDetails1.getEmail());
    assertEquals(member.getOauth2Provider(), customUserDetails1.getOauth2Provider());
    assertEquals(Role.USER, customUserDetails1.getRole());
    assertEquals(authority, customUserDetails1.getAuthorities());
  }

  @Test
  @DisplayName("Member 객체와 Attributes를 통한 CustomUserDetails 생성")
  void createCustomUserDetailsByMemberAndAttributes() {
    Map<String, Object> attributes = new HashMap<>();
    CustomUserDetails customUserDetails1 = CustomUserDetails.create(member, attributes);

    assertNotNull(customUserDetails1);
    assertEquals(member.getId(), customUserDetails1.getId());
    assertEquals(member.getEmail(), customUserDetails1.getEmail());
    assertEquals(member.getOauth2Provider(), customUserDetails1.getOauth2Provider());
    assertEquals(Role.USER, customUserDetails1.getRole());
    assertEquals(authority, customUserDetails1.getAuthorities());
    assertEquals(attributes, customUserDetails1.getAttributes());
  }

  @Test
  @DisplayName("Authorities 조회")
  void getAuthorities() {
    assertEquals(authority, customUserDetailsObject.getAuthorities());
  }

  @Test
  @DisplayName("Email 조회")
  void getUsername() {
    assertEquals("test@test.com", customUserDetailsObject.getUsername());
  }

  @Test
  @DisplayName("Password 조회")
  void getPassword() {
    assertNull(customUserDetailsObject.getPassword());
  }

  @Test
  @DisplayName("AccountNonExpired 여부 조회")
  void isAccountNonExpired() {
    assertTrue(customUserDetailsObject.isAccountNonExpired());
  }

  @Test
  @DisplayName("AccountNonLocked 여부 조회")
  void isAccountNonLocked() {
    assertTrue(customUserDetailsObject.isAccountNonLocked());
  }

  @Test
  @DisplayName("CredentialsNonExpired 여부 조회")
  void isCredentialsNonExpired() {
    assertTrue(customUserDetailsObject.isCredentialsNonExpired());
  }

  @Test
  @DisplayName("Enabled 여부 조회")
  void isEnabled() {
    assertTrue(customUserDetailsObject.isEnabled());
  }

  @Test
  @DisplayName("Id 조회")
  void getName() {
    assertEquals(Long.toString(1L), customUserDetailsObject.getName());
  }

  @Test
  @DisplayName("Attributes 조회")
  void getAttributes() {
    Map<String, Object> attributes = new HashMap<>();
    CustomUserDetails customUserDetails1 = CustomUserDetails.create(member, attributes);

    assertEquals(attributes, customUserDetails1.getAttributes());
  }

  @Test
  @DisplayName("Attributes 설정")
  void setAttributes() {
    Map<String, Object> attributes = new HashMap<>();
    customUserDetailsObject.setAttributes(attributes);

    assertEquals(attributes, customUserDetailsObject.getAttributes());
  }
}
