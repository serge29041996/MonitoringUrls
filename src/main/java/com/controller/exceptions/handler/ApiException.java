package com.controller.exceptions.handler;

import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;

import java.util.ArrayList;
import java.util.List;

/**
 * Class for saving exception information to response.
 */
@Data
public class ApiException {
  private HttpStatus status;
  private String message;
  private String debugMessage;
  private List<ApiValidationException> subErrors = new ArrayList<>();

  private ApiException() {
  }

  /**
   * Constructor for HttpStatus.
   * @param status status of response
   */
  public ApiException(HttpStatus status) {
    this();
    this.status = status;
  }

  /**
   * Constructor for HttpStatus, message and debug message.
   * @param status status of response
   * @param message message of exception
   * @param debugMessage debug message of exception
   */
  public ApiException(HttpStatus status, String message, String debugMessage) {
    this();
    this.status = status;
    this.message = message;
    this.debugMessage = debugMessage;
  }

  /**
   * Constructor for HttpStatus and exception field.
   * @param status status of response
   * @param message message of exception
   * @param ex exception
   */
  public ApiException(HttpStatus status, String message, Throwable ex) {
    this();
    this.status = status;
    this.message = message;
    this.debugMessage = ex.getLocalizedMessage();
  }

  private void addSubException(ApiValidationException subError) {
    subErrors.add(subError);
  }

  private void addValidationError(String object, String field, Object rejectedValue,
                                      String message) {
    addSubException(new ApiValidationException(object, field, rejectedValue, message));
  }

  private void addValidationException(FieldError fieldError) {
    this.addValidationError(
        fieldError.getObjectName(),
        fieldError.getField(),
        fieldError.getRejectedValue(),
        fieldError.getDefaultMessage());
  }

  void addValidationExceptions(List<FieldError> fieldErrors) {
    fieldErrors.forEach(this::addValidationException);
  }
}
