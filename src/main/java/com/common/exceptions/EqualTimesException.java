package com.common.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception throw when begin and end times is equals
 */
@ResponseStatus(HttpStatus.CONFLICT)
public class EqualTimesException extends RuntimeException {
  public EqualTimesException(String message) {
    super(message);
  }
}
