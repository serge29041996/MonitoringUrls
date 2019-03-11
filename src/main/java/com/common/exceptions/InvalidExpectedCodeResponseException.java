/*
 * Copyright (C) 2019.
 * This file is part of project MonitoringUrls
 * Written by Sergiy Krasnikov <sergei29041996@gmail.com>
 */

package com.common.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception for invalid expected code of response.
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidExpectedCodeResponseException extends ApiValidationException {
  /**
   * Constructor with message of exception.
   * @param message info message for exception
   */
  public InvalidExpectedCodeResponseException(String message) {
    super(message);
  }
}
