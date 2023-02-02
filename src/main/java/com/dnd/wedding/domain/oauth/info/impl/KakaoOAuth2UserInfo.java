package com.dnd.wedding.domain.oauth.info.impl;

import com.dnd.wedding.domain.oauth.info.OAuth2UserInfo;
import java.util.Map;

public class KakaoOAuth2UserInfo extends OAuth2UserInfo {

  private final Map<String, Object> kakaoAccount;
  private final Map<String, Object> profile;

  public KakaoOAuth2UserInfo(Map<String, Object> attributes) {
    super(attributes);

    this.kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
    this.profile = (Map<String, Object>) kakaoAccount.get("profile");
  }

  @Override
  public String getId() {
    return (String) attributes.get("id");
  }

  @Override
  public String getName() {
    return (String) this.profile.get("nickname");
  }

  @Override
  public String getEmail() {
    return (String) this.kakaoAccount.get("email");
  }

  @Override
  public String getImageUrl() {
    return (String) this.profile.get("profile_image_url");
  }
}
