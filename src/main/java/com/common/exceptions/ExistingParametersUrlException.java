package com.common.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception for existing same parameters in database.
 */
@ResponseStatus(HttpStatus.CONFLICT)
public class ExistingParametersUrlException extends RuntimeException {
  public ExistingParametersUrlException(String message) {
    super(message);
  }
}
