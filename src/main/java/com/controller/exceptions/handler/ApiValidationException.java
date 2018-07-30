package com.controller.exceptions.handler;

import lombok.Data;

/**
 * Class for saving validation exception field of class
 */
@Data
public class ApiValidationException {
  private String object;
  private String field;
  private Object rejectedValue;
  private String message;

  ApiValidationException() {

  }

  ApiValidationException(String object, String message) {
    this.object = object;
    this.message = message;
  }

  ApiValidationException(String object, String field, Object rejectedValue, String message) {
    this.object = object;
    this.field = field;
    this.rejectedValue = rejectedValue;
    this.message = message;
  }
}
