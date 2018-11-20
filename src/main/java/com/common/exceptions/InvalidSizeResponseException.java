package com.common.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception for invalid size of response (min size bigger than max).
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidSizeResponseException extends RuntimeException {
  public InvalidSizeResponseException(String message) {
    super(message);
  }
}
