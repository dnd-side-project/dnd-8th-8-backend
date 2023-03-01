package com.dnd.weddingmap.domain.member;

import com.dnd.weddingmap.domain.common.BaseTimeEntity;
import com.dnd.weddingmap.domain.oauth.OAuth2Provider;
import com.dnd.weddingmap.domain.wedding.Wedding;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "wedding_id")
  private Wedding wedding;

  @Builder
  public Member(
      Long id,
      String name,
      String email,
      String profileImage,
      Role role,
      OAuth2Provider oauth2Provider,
      Wedding wedding
  ) {
    this.id = id;
    this.name = name;
    this.email = email;
    this.profileImage = profileImage;
    this.role = role;
    this.oauth2Provider = oauth2Provider;
    this.wedding = wedding;
  }

  public void setGender(Gender gender) {
    this.gender = gender;
  }

  public void setProfileImage(String profileImage) {
    this.profileImage = profileImage;
  }

  public void setWedding(Wedding wedding) {
    this.wedding = wedding;
  }

  public void setName(String name) {
    this.name = name;
  }
}
