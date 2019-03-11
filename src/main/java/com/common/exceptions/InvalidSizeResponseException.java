/*
 * Copyright (C) 2019.
 * This file is part of project MonitoringUrls
 * Written by Sergiy Krasnikov <sergei29041996@gmail.com>
 */

package com.common.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception for invalid size of response (min size bigger than max).
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidSizeResponseException extends ApiValidationException {
  /**
   * Constructor with message of exception.
   * @param message info message for exception
   */
  public InvalidSizeResponseException(String message) {
    super(message);
  }
}
