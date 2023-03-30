package com.dnd.weddingmap.global.util;

import java.util.Locale;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

@Component
public class MessageUtil {

  private static MessageSource messageSource;

  @Autowired
  private MessageUtil(MessageSource messageSource) {
    MessageUtil.setMessageSource(messageSource);
  }

  public static String getMessage(String code) {
    return getMessageSource().getMessage(code, null, Locale.getDefault());
  }

  public static String getMessage(String code, Object[] args) {
    return getMessageSource().getMessage(code, args, Locale.getDefault());
  }

  public static MessageSource getMessageSource() {
    return messageSource;
  }

  public static void setMessageSource(MessageSource messageSource) {
    MessageUtil.messageSource = messageSource;
  }
}
