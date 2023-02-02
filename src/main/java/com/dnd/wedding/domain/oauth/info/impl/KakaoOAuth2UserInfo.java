package com.dnd.wedding.domain.oauth.info.impl;

import com.dnd.wedding.domain.oauth.info.OAuth2UserInfo;
import java.util.Map;

public class KakaoOAuth2UserInfo extends OAuth2UserInfo {

  public KakaoOAuth2UserInfo(Map<String, Object> attributes) {
    super(attributes);
  }

  @Override
  public String getId() {
    return (String) attributes.get("id");
  }

  @Override
  public String getName() {
    Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
    Map<String, Object> profile = (Map<String, Object>) kakaoAccount.get("profile");
    return (String) profile.get("nickname");
  }

  @Override
  public String getEmail() {
    Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
    return (String) kakaoAccount.get("email");
  }

  @Override
  public String getImageUrl() {
    Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
    Map<String, Object> profile = (Map<String, Object>) kakaoAccount.get("profile");
    return (String) profile.get("profile_image_url");
  }
}
