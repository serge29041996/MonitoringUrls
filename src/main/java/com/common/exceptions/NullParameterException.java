package com.common.exceptions;

/**
 * Exception where parameters is a null
 */
public class NullParameterException extends RuntimeException {
  public NullParameterException(String message) {
    super(message);
  }
}
