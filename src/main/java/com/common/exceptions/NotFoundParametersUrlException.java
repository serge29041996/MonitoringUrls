/*
 * Copyright (C) 2019.
 * This file is part of project MonitoringUrls
 * Written by Sergiy Krasnikov <sergei29041996@gmail.com>
 */

package com.common.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception for lack of parameters of url.
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class NotFoundParametersUrlException extends ApiValidationException {
  /**
   * Constructor with message of exception.
   * @param message info message for exception
   */
  public NotFoundParametersUrlException(String message) {
    super(message);
  }
}
