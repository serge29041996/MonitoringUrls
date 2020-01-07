/*
 * Copyright (C) 2019.
 * This file is part of project MonitoringUrls
 * Written by Sergiy Krasnikov <sergei29041996@gmail.com>
 */

package com.controller.exceptions.handler;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;

import com.common.exceptions.CompareTimesException;
import com.common.exceptions.EqualParametersException;
import com.common.exceptions.EqualTimesException;
import com.common.exceptions.ExistingParametersUrlException;
import com.common.exceptions.InvalidExpectedCodeResponseException;
import com.common.exceptions.InvalidSizeResponseException;
import com.common.exceptions.InvalidTimeResponseException;
import com.common.exceptions.NotFoundParametersUrlException;
import lombok.extern.log4j.Log4j;
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

/**
 * Class for handling all exception, which occurred in controller.
 */
@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
@Log4j
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

  /**
   * Handle MethodArgumentNotValidException. Triggered when an object fails @Valid validation.
   * @param ex      the MethodArgumentNotValidException that is thrown when @Valid validation fails
   * @param headers HttpHeaders
   * @param status  HttpStatus
   * @param request WebRequest
   * @return the ApiException object
   */
  @Override
  protected ResponseEntity<Object> handleMethodArgumentNotValid(
      MethodArgumentNotValidException ex,
      HttpHeaders headers,
      HttpStatus status,
      WebRequest request) {
    ApiExceptionData apiExceptionData = new ApiExceptionData(BAD_REQUEST);
    apiExceptionData.setMessage("Validation error");
    apiExceptionData.addValidationExceptions(ex.getBindingResult().getFieldErrors());
    return buildResponseEntity(apiExceptionData);
  }

  /**
   * Handle HttpMessageNotReadableException. Happens when request JSON is malformed.
   * @param ex      HttpMessageNotReadableException
   * @param headers HttpHeaders
   * @param status  HttpStatus
   * @param request WebRequest
   * @return the ApiException object
   */
  @Override
  protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
                                                                HttpHeaders headers,
                                                                HttpStatus status,
                                                                WebRequest request) {
    log.error("Exception with JSON:", ex);
    String error = "Malformed JSON request";
    return buildResponseEntity(new ApiExceptionData(BAD_REQUEST, error, ex));
  }

  /**
   * Handle MethodArgumentTypeMismatchException for invalid type parameter of request.
   * @param ex the MethodArgumentTypeMismatchException
   * @return the ApiException object
   */
  @ExceptionHandler(MethodArgumentTypeMismatchException.class)
  protected ResponseEntity<Object> handleMethodArgumentTypeMismatch(
      MethodArgumentTypeMismatchException ex, WebRequest request) {
    ApiExceptionData apiExceptionData = new ApiExceptionData(BAD_REQUEST);
    apiExceptionData.setMessage(String.format(
        "The parameter '%s' of value '%s' could not be converted to type '%s'",
        ex.getName(), ex.getValue(), ex.getRequiredType().getSimpleName()));
    apiExceptionData.setDebugMessage(ex.getMessage());
    return buildResponseEntity(apiExceptionData);
  }

  /**
   * Handle another exceptions.
   * @param ex the Exception
   * @param request WebRequest
   * @return the ApiException object
   */
  @ExceptionHandler({ Exception.class })
  public ResponseEntity<Object> handleAll(Throwable ex, WebRequest request) {
    ApiExceptionData apiExceptionData = new ApiExceptionData(
        INTERNAL_SERVER_ERROR, ex.getLocalizedMessage(), "error occurred");
    return buildResponseEntity(apiExceptionData);
  }

  /**
   * Handle bad request exceptions.
   * @param ex the exception for checking validation of parameter monitoring url
   * @return the ApiException object
   */
  @ExceptionHandler({CompareTimesException.class, EqualParametersException.class,
      EqualTimesException.class, InvalidExpectedCodeResponseException.class,
      InvalidSizeResponseException.class, InvalidTimeResponseException.class})
  protected ResponseEntity<Object> handleBadRequest(Exception ex) {
    ApiExceptionData apiExceptionData = new ApiExceptionData(BAD_REQUEST);
    apiExceptionData.setMessage(ex.getMessage());
    return buildResponseEntity(apiExceptionData);
  }

  /**
   * Handle existing parameter monitoring url in database.
   * @param ex the ExistingParametersUrlException
   * @return the ApiException object
   */
  @ExceptionHandler(ExistingParametersUrlException.class)
  protected ResponseEntity<Object> handleConflict(ExistingParametersUrlException ex) {
    ApiExceptionData apiExceptionData = new ApiExceptionData(CONFLICT);
    apiExceptionData.setMessage(ex.getMessage());
    return buildResponseEntity(apiExceptionData);
  }

  /**
   * Handle not found parameter monitoring url in database.
   * @param ex the NotFoundParametersUrlException
   * @return the ApiException object
   */
  @ExceptionHandler(NotFoundParametersUrlException.class)
  protected ResponseEntity<Object> handleNotFound(NotFoundParametersUrlException ex) {
    ApiExceptionData apiExceptionData = new ApiExceptionData(NOT_FOUND);
    apiExceptionData.setMessage(ex.getMessage());
    return buildResponseEntity(apiExceptionData);
  }

  private static ResponseEntity<Object> buildResponseEntity(ApiExceptionData apiExceptionData) {
    return new ResponseEntity<>(apiExceptionData, apiExceptionData.getStatus());
  }
}
