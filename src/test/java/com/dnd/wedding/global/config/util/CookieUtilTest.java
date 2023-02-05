package com.dnd.wedding.global.config.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mockStatic;

import jakarta.servlet.http.Cookie;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.event.annotation.AfterTestClass;
import org.springframework.test.context.event.annotation.BeforeTestClass;


class CookieUtilTest {

  private static MockedStatic<CookieUtil> cookieUtil;
  private MockHttpServletRequest request;
  private MockHttpServletResponse response;

  @BeforeTestClass
  public void beforeTestClass() {
    cookieUtil = mockStatic(CookieUtil.class);
  }

  @AfterTestClass
  public void afterTestClass() {
    cookieUtil.close();
  }

  @BeforeEach
  void init() {
    this.request = new MockHttpServletRequest();
    this.response = new MockHttpServletResponse();
  }

  @Test
  @DisplayName("Request에 유효한 Cookie가 있는 경우 해당 Cookie를 반환한다.")
  void returnCookieByValidRequest() {
    Cookie cookie = new Cookie("testName", "testValue");
    request.setCookies(cookie);

    CookieUtil.getCookie(request, "testName").ifPresent(c -> {
      assertEquals("testName", c.getName());
      assertEquals("testValue", c.getValue());
    });
  }

  @Test
  @DisplayName("Request에 Cookie가 없는 경우 Empty를 반환한다.")
  void returnEmptyByInvalidRequest() {
    Optional<Cookie> optionalCookie = CookieUtil.getCookie(request, "testName");

    assertEquals(Optional.empty(), optionalCookie);
  }

  @Test
  @DisplayName("Cookie 추가")
  void addCookieToResponse() {
    CookieUtil.addCookie(response, "testName", "testValue", 1000);

    Cookie[] cookies = response.getCookies();

    assertEquals(1, cookies.length);
    assertEquals("testName", cookies[0].getName());
    assertEquals("testValue", cookies[0].getValue());
    assertEquals("/", cookies[0].getPath());
    assertEquals(1000, cookies[0].getMaxAge());
  }

  @Test
  @DisplayName("Cookie 삭제")
  void deleteCookieToResponse() {
    Cookie cookie = new Cookie("testName", "testValue");
    request.setCookies(cookie);

    CookieUtil.deleteCookie(request, response, "testName");

    Cookie[] cookies = response.getCookies();

    assertEquals(1, cookies.length);
    assertEquals("", cookies[0].getValue());
    assertEquals("/", cookies[0].getPath());
    assertEquals(0, cookies[0].getMaxAge());
  }
}
