package com.dnd.wedding.global;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import org.springframework.security.test.context.support.WithSecurityContext;

@Retention(RetentionPolicy.RUNTIME)
@WithSecurityContext(factory = WithMockOAuthUserSecurityContextFactory.class)
public @interface WithMockOAuthUser {

}
