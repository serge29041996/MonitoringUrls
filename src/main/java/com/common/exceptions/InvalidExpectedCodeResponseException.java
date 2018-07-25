package com.common.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception for invalid expected code of response
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidExpectedCodeResponseException extends RuntimeException {
  public InvalidExpectedCodeResponseException(String message) {
    super(message);
  }
}
