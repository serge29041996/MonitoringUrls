package com.common.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception for lack of parameters of url.
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class NotFoundParametersUrlException extends RuntimeException {
  public NotFoundParametersUrlException(String message) {
    super(message);
  }
}
