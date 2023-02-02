package com.dnd.wedding.domain.jwt.handler;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;
import org.springframework.security.access.AccessDeniedException;

class JwtAccessDeniedHandlerTest {

  @Test
  void shouldReturnForbidden() throws Exception {
    JwtAccessDeniedHandler handler = new JwtAccessDeniedHandler();
    HttpServletRequest request = mock(HttpServletRequest.class);
    HttpServletResponse response = mock(HttpServletResponse.class);
    AccessDeniedException exception = new AccessDeniedException("Forbidden");

    handler.handle(request, response, exception);

    verify(response).sendError(403);
  }
}
