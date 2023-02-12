package com.dnd.wedding.global.exception;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.dnd.wedding.global.response.ErrorMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

class ControllerExceptionHandlerTest {

  private ControllerExceptionHandler controllerExceptionHandler;

  @BeforeEach
  public void init() throws Exception {
    controllerExceptionHandler = new ControllerExceptionHandler();
  }

  @Test
  @DisplayName("NotFoundException 발생 테스트")
  void notFoundExceptionTest() {
    String message = "Not Found";
    NotFoundException ex = new NotFoundException(message);
    ResponseEntity<ErrorMessage> error = controllerExceptionHandler.notFoundException(ex);

    assertNotNull(error);
    assertEquals(HttpStatus.NOT_FOUND, error.getStatusCode());
    assertEquals(message, error.getBody().getMessage());
  }

  @Test
  @DisplayName("BadRequestException 발생 테스트")
  void badRequestExceptionTest() {
    String message = "Bad request";
    BadRequestException ex = new BadRequestException(message);
    ResponseEntity<ErrorMessage> error = controllerExceptionHandler.badRequestException(ex);

    assertNotNull(error);
    assertEquals(HttpStatus.BAD_REQUEST, error.getStatusCode());
    assertEquals(message, error.getBody().getMessage());
  }

  @Test
  @DisplayName("UnauthorizedException 발생 테스트")
  void unauthorizedUserExceptionTest() {
    String message = "Unauthorized user";
    UnauthorizedException ex = new UnauthorizedException(message);
    ResponseEntity<ErrorMessage> error = controllerExceptionHandler.unauthorizedUserException(ex);

    assertNotNull(error);
    assertEquals(HttpStatus.UNAUTHORIZED, error.getStatusCode());
    assertEquals(message, error.getBody().getMessage());
  }

  @Test
  @DisplayName("ForbiddenException 발생 테스트")
  void forbiddenRequestExceptionTest() {
    String message = "Forbidden";
    ForbiddenException ex = new ForbiddenException(message);
    ResponseEntity<ErrorMessage> error = controllerExceptionHandler.forbiddenRequestException(ex);

    assertNotNull(error);
    assertEquals(HttpStatus.FORBIDDEN, error.getStatusCode());
    assertEquals(message, error.getBody().getMessage());
  }

  @Test
  @DisplayName("InternalServerErrorException 발생 테스트")
  void internalServerErrorExceptionTest() {
    String message = "Internal Server Error";
    InternalServerErrorException ex = new InternalServerErrorException(message);
    ResponseEntity<ErrorMessage> error = controllerExceptionHandler.internalServerErrorException(
        ex);

    assertNotNull(error);
    assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, error.getStatusCode());
    assertEquals(message, error.getBody().getMessage());
  }
}
