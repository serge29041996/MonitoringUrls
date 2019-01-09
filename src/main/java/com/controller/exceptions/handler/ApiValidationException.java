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
public class ApiValidationException {
  private String object;
  private String field;
  private Object rejectedValue;
  private String message;
}
