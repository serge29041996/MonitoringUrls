package com.common.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception for invalid times of response for different responses.
  */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidTimeResponseException extends RuntimeException {
  public InvalidTimeResponseException(String message) {
    super(message);
  }
}
