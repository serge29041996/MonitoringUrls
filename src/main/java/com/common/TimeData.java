package com.common;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Class for saving information about time (hour, minute and second).
 */
@Data
@AllArgsConstructor
public class TimeData {
  private int hour;
  private int minute;
  private int second;
}
