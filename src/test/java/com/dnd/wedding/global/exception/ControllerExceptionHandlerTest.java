package com.dnd.wedding.global.exception;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.dnd.wedding.global.response.ErrorResponse;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;

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
    ResponseEntity<ErrorResponse> error = controllerExceptionHandler.notFoundException(ex);

    assertNotNull(error);
    assertEquals(HttpStatus.NOT_FOUND, error.getStatusCode());
    assertEquals(message, error.getBody().getMessage());
  }

  @Test
  @DisplayName("BadRequestException 발생 테스트")
  void badRequestExceptionTest() {
    String message = "Bad request";
    BadRequestException ex = new BadRequestException(message);
    ResponseEntity<ErrorResponse> error = controllerExceptionHandler.badRequestException(ex);

    assertNotNull(error);
    assertEquals(HttpStatus.BAD_REQUEST, error.getStatusCode());
    assertEquals(message, error.getBody().getMessage());
  }

  @Test
  @DisplayName("UnauthorizedException 발생 테스트")
  void unauthorizedUserExceptionTest() {
    String message = "Unauthorized user";
    UnauthorizedException ex = new UnauthorizedException(message);
    ResponseEntity<ErrorResponse> error = controllerExceptionHandler.unauthorizedUserException(ex);

    assertNotNull(error);
    assertEquals(HttpStatus.UNAUTHORIZED, error.getStatusCode());
    assertEquals(message, error.getBody().getMessage());
  }

  @Test
  @DisplayName("ForbiddenException 발생 테스트")
  void forbiddenRequestExceptionTest() {
    String message = "Forbidden";
    ForbiddenException ex = new ForbiddenException(message);
    ResponseEntity<ErrorResponse> error = controllerExceptionHandler.forbiddenRequestException(ex);

    assertNotNull(error);
    assertEquals(HttpStatus.FORBIDDEN, error.getStatusCode());
    assertEquals(message, error.getBody().getMessage());
  }

  @Test
  @DisplayName("InternalServerErrorException 발생 테스트")
  void internalServerErrorExceptionTest() {
    String message = "Internal Server Error";
    InternalServerErrorException ex = new InternalServerErrorException(message);
    ResponseEntity<ErrorResponse> error = controllerExceptionHandler.internalServerErrorException(
        ex);

    assertNotNull(error);
    assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, error.getStatusCode());
    assertEquals(message, error.getBody().getMessage());
  }

  @Test
  @DisplayName("Exception 발생 테스트")
  void exceptionTest() {
    ResponseEntity<ErrorResponse> error = controllerExceptionHandler.handleException(
        new Exception());

    assertNotNull(error);
    assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, error.getStatusCode());
  }

  @Test
  @DisplayName("MethodArgumentNotValidException 발생 테스트")
  void methodArgumentNotValidExceptionTest() throws Exception {
    String message = "MethodArgumentNotValidException Error";

    MethodArgumentNotValidException exception = new MethodArgumentNotValidException(
        mock(MethodParameter.class),
        mock(BindingResult.class));

    ObjectError e = mock(ObjectError.class);
    when(exception.getAllErrors()).thenReturn(List.of(e));
    when(e.getDefaultMessage()).thenReturn(message);

    ResponseEntity<ErrorResponse> error = controllerExceptionHandler
        .handleNotBlankValid(exception);

    assertNotNull(error);
    assertEquals(HttpStatus.BAD_REQUEST, error.getStatusCode());
    assertEquals(message, error.getBody().getMessage());
  }

  @Test
  @DisplayName("HttpRequestMethodNotSupportedException 발생 테스트")
  void httpRequestMethodNotSupportedExceptionTest() throws Exception {
    HttpRequestMethodNotSupportedException exception = mock(
        HttpRequestMethodNotSupportedException.class);

    ResponseEntity<ErrorResponse> error = controllerExceptionHandler
        .httpRequestMethodNotSupportedException(exception);

    assertNotNull(error);
    assertEquals(HttpStatus.NOT_FOUND, error.getStatusCode());
  }
}
