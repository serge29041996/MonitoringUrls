package com.controller.exceptions.handler;

import lombok.Data;
import org.hibernate.validator.internal.engine.path.PathImpl;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;

import javax.validation.ConstraintViolation;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Class for saving exception information to response
 */
@Data
public class ApiException {
  private HttpStatus status;
  private String message;
  private String debugMessage;
  private List<ApiValidationException> subExceptions = new ArrayList<>();

  private ApiException() {
  }

  public ApiException(HttpStatus status) {
    this();
    this.status = status;
  }

  public ApiException(HttpStatus status, String message) {
    this();
    this.status = status;
    this.message = message;
  }

  public ApiException(HttpStatus status, Throwable ex) {
    this();
    this.status = status;
    this.message = "Unexpected error";
    this.debugMessage = ex.getLocalizedMessage();
  }

  public ApiException(HttpStatus status, String message, Throwable ex) {
    this();
    this.status = status;
    this.message = message;
    this.debugMessage = ex.getLocalizedMessage();
  }

  private void addSubException(ApiValidationException subError) {
    subExceptions.add(subError);
  }

  private void addValidationException(String object, String field, Object rejectedValue, String message) {
    addSubException(new ApiValidationException(object, field, rejectedValue, message));
  }

  private void addValidationException(String object, String message) {
    addSubException(new ApiValidationException(object, message));
  }

  private void addValidationException(FieldError fieldError) {
    this.addValidationException(
        fieldError.getObjectName(),
        fieldError.getField(),
        fieldError.getRejectedValue(),
        fieldError.getDefaultMessage());
  }

  void addValidationExceptions(List<FieldError> fieldErrors) {
    fieldErrors.forEach(this::addValidationException);
  }

  private void addValidationException(ObjectError objectError) {
    this.addValidationException(
        objectError.getObjectName(),
        objectError.getDefaultMessage());
  }

  void addValidationException(List<ObjectError> globalErrors) {
    globalErrors.forEach(this::addValidationException);
  }

  /**
   * Utility method for adding exception of ConstraintViolation. Usually when a @Validated validation fails.
   * @param cv the ConstraintViolation
   */
  private void addValidationException(ConstraintViolation<?> cv) {
    this.addValidationException(
        cv.getRootBeanClass().getSimpleName(),
        ((PathImpl) cv.getPropertyPath()).getLeafNode().asString(),
        cv.getInvalidValue(),
        cv.getMessage());
  }

  void addValidationExceptions(Set<ConstraintViolation<?>> constraintViolations) {
    constraintViolations.forEach(this::addValidationException);
  }
}
