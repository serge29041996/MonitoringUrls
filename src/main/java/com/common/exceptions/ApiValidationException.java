/*
 * Copyright (C) 2019.
 * This file is part of project MonitoringUrls
 * Written by Sergiy Krasnikov <sergei29041996@gmail.com>
 */

package com.common.exceptions;

/**
 * Common exception class for validation exceptions.
 */
public class ApiValidationException extends Exception {
  /**
   * Constructor with message of exception.
   * @param message info message for exception
   */
  public ApiValidationException(String message) {
    super(message);
  }
}
