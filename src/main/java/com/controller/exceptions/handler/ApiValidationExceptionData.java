/*
 * Copyright (C) 2019.
 * This file is part of project MonitoringUrls
 * Written by Sergiy Krasnikov <sergei29041996@gmail.com>
 */

package com.controller.exceptions.handler;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Class for saving validation exception field of class.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiValidationExceptionData {
  /** Object of validation error. */
  private String object;
  /** Field, where validation error was occurred. */
  private String field;
  /** Cause, why validation error was occurred. */
  private Object rejectedValue;
  /** Message of validation error. */
  private String message;
}
