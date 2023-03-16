package com.dnd.weddingmap.global.util;

import java.util.Locale;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;

@RequiredArgsConstructor
public class MessageUtil {
  private static MessageSource messageSource;

  public static String getMessage(String code) {
    return messageSource.getMessage(code, null, Locale.getDefault());
  }

  public static String getMessage(String code, Object[] args) {
    return messageSource.getMessage(code, args, Locale.getDefault());
  }
}
