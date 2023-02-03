package com.dnd.wedding.domain.oauth.info;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;

import com.dnd.wedding.domain.oauth.OAuth2Provider;
import com.dnd.wedding.domain.oauth.info.impl.GoogleOAuth2UserInfo;
import com.dnd.wedding.domain.oauth.info.impl.KakaoOAuth2UserInfo;
import java.util.Map;
import org.junit.jupiter.api.Test;

class OAuth2UserInfoFactoryTest {

  @Test
  void getKakaoUserinfo() {
    // given
    OAuth2Provider kakao = OAuth2Provider.KAKAO;

    Map<String, Object> profile = Map.of(
        "nickname", anyString(),
        "profile_image_url", anyString());
    Map<String, Object> kakaoAccount = Map.of(
        "email", anyString());
    Map<String, Object> attributes = Map.of(
        "id", anyInt(), "kakao_account", kakaoAccount, "profile", profile);

    // when
    OAuth2UserInfo userInfo = OAuth2UserInfoFactory.getOAuth2Userinfo(kakao, attributes);

    // then
    assertEquals(KakaoOAuth2UserInfo.class, userInfo.getClass());
  }
}