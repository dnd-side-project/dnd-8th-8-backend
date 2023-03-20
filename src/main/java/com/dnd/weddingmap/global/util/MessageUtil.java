package com.dnd.weddingmap.global.util;

import java.util.Locale;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

@Component
public class MessageUtil {

  static MessageSource messageSource;

  @Autowired
  private MessageUtil(MessageSource messageSource) {
    MessageUtil.messageSource = messageSource;
  }

  public static String getMessage(String code) {
    return messageSource.getMessage(code, null, Locale.getDefault());
  }

  public static String getMessage(String code, Object[] args) {
    return messageSource.getMessage(code, args, Locale.getDefault());
  }
}
