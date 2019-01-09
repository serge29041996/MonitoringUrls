package com.controller.exceptions.handler;

import com.common.exceptions.*;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import static org.springframework.http.HttpStatus.*;

@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

  /**
   * Handle MethodArgumentNotValidException. Triggered when an object fails @Valid validation.
   *
   * @param ex      the MethodArgumentNotValidException that is thrown when @Valid validation fails
   * @param headers HttpHeaders
   * @param status  HttpStatus
   * @param request WebRequest
   * @return the ApiError object
   */
  @Override
  protected ResponseEntity<Object> handleMethodArgumentNotValid(
      MethodArgumentNotValidException ex,
      HttpHeaders headers,
      HttpStatus status,
      WebRequest request) {
    ApiException apiException = new ApiException(BAD_REQUEST);
    apiException.setMessage("Validation error");
    apiException.addValidationExceptions(ex.getBindingResult().getFieldErrors());
    return buildResponseEntity(apiException);
  }

  /**
   * Handle HttpMessageNotReadableException. Happens when request JSON is malformed.
   *
   * @param ex      HttpMessageNotReadableException
   * @param headers HttpHeaders
   * @param status  HttpStatus
   * @param request WebRequest
   * @return the ApiError object
   */
  @Override
  protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
                                                                HttpHeaders headers,
                                                                HttpStatus status,
                                                                WebRequest request) {
    String error = "Malformed JSON request";
    return buildResponseEntity(new ApiException(BAD_REQUEST, error, ex));
  }

  /**
   * Handle MethodArgumentTypeMismatchException for invalid type parameter of request
   *
   * @param ex the MethodArgumentTypeMismatchException
   * @return the ApiError object
   */
  @ExceptionHandler(MethodArgumentTypeMismatchException.class)
  protected ResponseEntity<Object> handleMethodArgumentTypeMismatch(
      MethodArgumentTypeMismatchException ex, WebRequest request) {
    ApiException apiException = new ApiException(BAD_REQUEST);
    apiException.setMessage(String.format(
        "The parameter '%s' of value '%s' could not be converted to type '%s'",
        ex.getName(), ex.getValue(), ex.getRequiredType().getSimpleName()));
    apiException.setDebugMessage(ex.getMessage());
    return buildResponseEntity(apiException);
  }

  /**
   * Handle bad request exceptions
   * @param ex the exception for checking validation of parameter monitoring url
   * @return the ApiError object
   */
  @ExceptionHandler({CompareTimesException.class, EqualParametersException.class,
      EqualTimesException.class, InvalidExpectedCodeResponseException.class,
      InvalidSizeResponseException.class, InvalidTimeResponseException.class})
  protected ResponseEntity<Object> handleBadRequest(Exception ex) {
    ApiException apiException = new ApiException(BAD_REQUEST);
    apiException.setMessage(ex.getMessage());
    return buildResponseEntity(apiException);
  }

  /**
   * Handle existing parameter monitoring url in database
   * @param ex the ExistingParametersUrlException
   * @return the ApiError object
   */
  @ExceptionHandler(ExistingParametersUrlException.class)
  protected ResponseEntity<Object> handleConflict(ExistingParametersUrlException ex) {
    ApiException apiException = new ApiException(CONFLICT);
    apiException.setMessage(ex.getMessage());
    return buildResponseEntity(apiException);
  }

  /**
   * Handle not found parameter monitoring url in database
   * @param ex the NotFoundParametersUrlException
   * @return the ApiError object
   */
  @ExceptionHandler(NotFoundParametersUrlException.class)
  protected ResponseEntity<Object> handleNotFound(NotFoundParametersUrlException ex) {
    ApiException apiException = new ApiException(NOT_FOUND);
    apiException.setMessage(ex.getMessage());
    return buildResponseEntity(apiException);
  }

  private ResponseEntity<Object> buildResponseEntity(ApiException apiException) {
    return new ResponseEntity<>(apiException, apiException.getStatus());
  }

}