package com.dnd.wedding.domain.oauth.info;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.dnd.wedding.domain.oauth.OAuth2Provider;
import com.dnd.wedding.domain.oauth.info.impl.KakaoOAuth2UserInfo;
import java.util.Map;
import org.junit.jupiter.api.Test;

class OAuth2UserInfoFactoryTest {

  @Test
  void getKakaoUserinfo() {
    // given
    OAuth2Provider kakao = OAuth2Provider.KAKAO;

    Map<String, Object> profile = Map.of("nickname", "test", "profile_image_url", "test");
    Map<String, Object> kakaoAccount = Map.of("email", "test@example.com");
    Map<String, Object> attributes = Map.of(
        "id", "1234", "kakao_account", kakaoAccount, "profile", profile);

    // when
    OAuth2UserInfo userInfo = OAuth2UserInfoFactory.getOAuth2Userinfo(kakao, attributes);

    // then
    assertEquals(KakaoOAuth2UserInfo.class, userInfo.getClass());
  }
}