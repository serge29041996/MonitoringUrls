/*
 * Copyright (C) 2019.
 * This file is part of project MonitoringUrls
 * Written by Sergiy Krasnikov <sergei29041996@gmail.com>
 */

package com.common.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception throw when begin and end times is equals.
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class EqualTimesException extends ApiValidationException {
  /**
   * Constructor with message of exception.
   * @param message info message for exception
   */
  public EqualTimesException(String message) {
    super(message);
  }
}
