package com.dnd.weddingmap.domain.oauth.info;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.dnd.weddingmap.domain.oauth.OAuth2Provider;
import com.dnd.weddingmap.domain.oauth.info.impl.GoogleOAuth2UserInfo;
import com.dnd.weddingmap.domain.oauth.info.impl.KakaoOAuth2UserInfo;
import java.util.Map;
import org.junit.jupiter.api.Test;

class OAuth2UserInfoFactoryTest {

  @Test
  void getGoogleUserinfo() {
    // given
    OAuth2Provider google = OAuth2Provider.GOOGLE;

    Map<String, Object> attributes = Map.of(
        "sub", 1,
        "name", "test_name",
        "email", "test@test.com",
        "picture", "test_picture");

    // when
    OAuth2UserInfo userInfo = OAuth2UserInfoFactory.getOAuth2Userinfo(google, attributes);

    // then
    assertEquals(GoogleOAuth2UserInfo.class, userInfo.getClass());
  }

  @Test
  void getKakaoUserinfo() {
    // given
    OAuth2Provider kakao = OAuth2Provider.KAKAO;

    Map<String, Object> profile = Map.of(
        "nickname", "test_nickname",
        "profile_image_url", "test_image_url");
    Map<String, Object> kakaoAccount = Map.of(
        "email", "test@test.com");
    Map<String, Object> attributes = Map.of(
        "id", 1, "kakao_account", kakaoAccount, "profile", profile);

    // when
    OAuth2UserInfo userInfo = OAuth2UserInfoFactory.getOAuth2Userinfo(kakao, attributes);

    // then
    assertEquals(KakaoOAuth2UserInfo.class, userInfo.getClass());
  }
}
