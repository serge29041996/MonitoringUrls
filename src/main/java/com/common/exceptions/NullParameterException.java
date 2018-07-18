package com.common.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception where parameters is a null
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class NullParameterException extends RuntimeException {
  public NullParameterException(String message) {
    super(message);
  }
}
