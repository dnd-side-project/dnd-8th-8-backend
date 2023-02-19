package com.dnd.wedding.global.exception;

import com.dnd.wedding.global.response.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ControllerExceptionHandler {

  @ExceptionHandler(NotFoundException.class)
  public ResponseEntity<ErrorResponse> notFoundException(NotFoundException ex) {
    ErrorResponse message = new ErrorResponse(
        HttpStatus.NOT_FOUND.value(),
        ex.getMessage());

    return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler(BadRequestException.class)
  public ResponseEntity<ErrorResponse> badRequestException(BadRequestException ex) {
    ErrorResponse message = new ErrorResponse(
        HttpStatus.BAD_REQUEST.value(),
        ex.getMessage());

    return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(UnauthorizedException.class)
  public ResponseEntity<ErrorResponse> unauthorizedUserException(UnauthorizedException ex) {
    ErrorResponse message = new ErrorResponse(
        HttpStatus.UNAUTHORIZED.value(),
        ex.getMessage());

    return new ResponseEntity<>(message, HttpStatus.UNAUTHORIZED);
  }

  @ExceptionHandler(ForbiddenException.class)
  public ResponseEntity<ErrorResponse> forbiddenRequestException(ForbiddenException ex) {
    ErrorResponse message = new ErrorResponse(
        HttpStatus.FORBIDDEN.value(),
        ex.getMessage());

    return new ResponseEntity<>(message, HttpStatus.FORBIDDEN);
  }

  @ExceptionHandler(InternalServerErrorException.class)
  public ResponseEntity<ErrorResponse> internalServerErrorException(
      InternalServerErrorException ex) {
    ErrorResponse message = new ErrorResponse(
        HttpStatus.INTERNAL_SERVER_ERROR.value(),
        ex.getMessage());

    return new ResponseEntity<>(message, HttpStatus.INTERNAL_SERVER_ERROR);
  }

  @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
  public ResponseEntity<ErrorResponse> httpRequestMethodNotSupportedException(
      final HttpRequestMethodNotSupportedException ex) {

    ErrorResponse message = new ErrorResponse(
        HttpStatus.NOT_FOUND.value(),
        ex.getMessage());

    return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  protected ResponseEntity<ErrorResponse> methodArgumentNotValidException(MethodArgumentNotValidException ex) {
    ErrorResponse message = new ErrorResponse(
        HttpStatus.BAD_REQUEST.value(),
        ex.getAllErrors().get(0).getDefaultMessage());

    return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(Exception.class)
  protected ResponseEntity<ErrorResponse> handleException(Exception ex) {
    ErrorResponse message = new ErrorResponse(
        HttpStatus.INTERNAL_SERVER_ERROR.value(),
        ex.getMessage());

    return new ResponseEntity<>(message, HttpStatus.INTERNAL_SERVER_ERROR);
  }
}
