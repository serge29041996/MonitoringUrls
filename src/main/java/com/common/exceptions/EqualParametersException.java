package com.common.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception for equal fields of the parameter.
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class EqualParametersException extends RuntimeException {
  public EqualParametersException(String message) {
    super(message);
  }
}
