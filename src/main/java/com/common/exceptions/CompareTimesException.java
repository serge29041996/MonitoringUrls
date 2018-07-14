package com.common.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception is throw when one time object has a small time (hour or hour and minute) than another time object
 */
@ResponseStatus(HttpStatus.CONFLICT)
public class CompareTimesException extends RuntimeException {
  public CompareTimesException(String message) {
    super(message);
  }
}
