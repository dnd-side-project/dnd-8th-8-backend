package com.dnd.wedding.global.util;

import jakarta.servlet.http.HttpServletRequest;

public class HeaderUtil {

  private static final String HEADER_AUTHORIZATION = "Authorization";
  private static final String TOKEN_PREFIX = "Bearer ";

  private HeaderUtil() {
    throw new IllegalStateException("Utility class");
  }

  public static String getAccessToken(HttpServletRequest request) {
    String bearerToken = request.getHeader(HEADER_AUTHORIZATION);

    if (bearerToken.startsWith(TOKEN_PREFIX)) {
      return bearerToken.substring(TOKEN_PREFIX.length());
    }

    return null;
  }
}
