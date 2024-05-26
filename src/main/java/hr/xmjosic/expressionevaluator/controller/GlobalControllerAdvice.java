package hr.xmjosic.expressionevaluator.controller;

import hr.xmjosic.expressionevaluator.dto.ErrorDto;
import jakarta.servlet.http.HttpServletRequest;
import java.time.Instant;
import java.util.NoSuchElementException;

import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

/**
 * GlobalControllerAdvice is a class that serves as a global exception handler for all controllers
 * in a Spring application.
 */
@Primary
@RestControllerAdvice
public class GlobalControllerAdvice {

  /**
   * Handles the HttpMediaTypeNotSupportedException and returns an ErrorDto with the appropriate
   * details.
   *
   * @param ex the HttpMediaTypeNotSupportedException that was thrown
   * @param request the HttpServletRequest object representing the current request
   * @return an ErrorDto object containing the timestamp, status code, error class name, error
   *     message, and request path
   */
  @ResponseStatus(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
  @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
  public ErrorDto handleHttpMediaTypeNotSupportedException(
      HttpMediaTypeNotSupportedException ex, HttpServletRequest request) {
    return ErrorDto.builder()
        .timestamp(Instant.now())
        .status(ex.getStatusCode().value())
        .error(ex.getClass().getName())
        .message(ex.getMessage())
        .path(request.getRequestURI())
        .build();
  }

  /**
   * Handles the HttpMessageNotReadableException and returns an ErrorDto with the appropriate
   * details.
   *
   * @param ex the HttpMessageNotReadableException that was thrown
   * @param request the HttpServletRequest object representing the current request
   * @return an ErrorDto object containing the timestamp, status code, error class name, error
   *     message, and request path
   */
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler(HttpMessageNotReadableException.class)
  public ErrorDto handleHttpMessageNotReadableException(
      HttpMessageNotReadableException ex, HttpServletRequest request) {
    return ErrorDto.builder()
        .timestamp(Instant.now())
        .status(HttpStatus.BAD_REQUEST.value())
        .error(ex.getClass().getName())
        .message(ex.getMessage())
        .path(request.getRequestURI())
        .build();
  }

  /**
   * Handles the NoResourceFoundException and returns an ErrorDto with the appropriate details.
   *
   * @param ex the NoResourceFoundException that was thrown
   * @param request the HttpServletRequest object representing the current request
   * @return an ErrorDto object containing the timestamp, status code, error class name, error
   *     message, and request path
   */
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler(NoResourceFoundException.class)
  public ErrorDto handleNoResourceFoundException(NoResourceFoundException ex, HttpServletRequest request) {
    return ErrorDto.builder()
        .timestamp(Instant.now())
        .status(HttpStatus.BAD_REQUEST.value())
        .error(ex.getClass().getName())
        .message(ex.getMessage())
        .path(request.getRequestURI())
        .build();
  }

  /**
   * Handles the NoSuchElementException and returns an ErrorDto with the appropriate details.
   *
   * @param ex the NoSuchElementException that was thrown
   * @param request the HttpServletRequest object representing the current request
   * @return an ErrorDto object containing the timestamp, status code, error class name, error
   *     message, and request path
   */
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler(NoSuchElementException.class)
  public ErrorDto handleNoSuchElementException(NoSuchElementException ex, HttpServletRequest request) {
    return ErrorDto.builder()
            .timestamp(Instant.now())
            .status(HttpStatus.BAD_REQUEST.value())
            .error(ex.getClass().getName())
            .message(ex.getMessage())
            .path(request.getRequestURI())
            .build();
  }

  /**
   * Handles the NoSuchElementException and returns an ErrorDto with the appropriate details.
   *
   * @param ex the NoSuchElementException that was thrown
   * @param request the HttpServletRequest object representing the current request
   * @return an ErrorDto object containing the timestamp, status code, error class name, error
   *     message, and request path
   */
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler(IllegalArgumentException.class)
  public ErrorDto handleIllegalArgumentException(IllegalArgumentException ex, HttpServletRequest request) {
    return ErrorDto.builder()
            .timestamp(Instant.now())
            .status(HttpStatus.BAD_REQUEST.value())
            .error(ex.getClass().getName())
            .message(ex.getMessage())
            .path(request.getRequestURI())
            .build();
  }

  /**
   * Handles the Throwable exception and returns an ErrorDto with the appropriate details.
   *
   * @param ex the Throwable exception that was thrown
   * @param request the HttpServletRequest object representing the current request
   * @return an ErrorDto object containing the timestamp, status code, error class name, error
   *     message, and request path
   */
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  @ExceptionHandler(Throwable.class)
  public ErrorDto handleThrowable(Throwable ex, HttpServletRequest request) {
    return ErrorDto.builder()
        .timestamp(Instant.now())
        .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
        .error(ex.getClass().getName())
        .message(ex.getMessage())
        .path(request.getRequestURI())
        .build();
  }
}
