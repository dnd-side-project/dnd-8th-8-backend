package com.dnd.weddingmap.domain.member;

import com.dnd.weddingmap.domain.common.BaseTimeEntity;
import com.dnd.weddingmap.domain.oauth.OAuth2Provider;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
@Table(name = "MEMBER")
public class Member extends BaseTimeEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(length = 64, nullable = false)
  private String name;

  @Column(length = 64, unique = true, nullable = false)
  private String email;

  @Column(length = 512)
  private String profileImage;

  @Column(nullable = false)
  @Enumerated(value = EnumType.STRING)
  private Role role;

  @Column(length = 20, nullable = false)
  @Enumerated(value = EnumType.STRING)
  private OAuth2Provider oauth2Provider;

  @Column(length = 10)
  @Enumerated(value = EnumType.STRING)
  private Gender gender;

  @Builder
  public Member(
      Long id,
      String name,
      String email,
      String profileImage,
      Role role,
      OAuth2Provider oauth2Provider
  ) {
    this.id = id;
    this.name = name;
    this.email = email;
    this.profileImage = profileImage;
    this.role = role;
    this.oauth2Provider = oauth2Provider;
  }

  public void setGender(Gender gender) {
    this.gender = gender;
  }

  public void setProfileImage(String profileImage) {
    this.profileImage = profileImage;
  }

  public Member update(String name, String profileImage) {
    this.name = name;
    this.profileImage = profileImage;
    return this;
  }
}