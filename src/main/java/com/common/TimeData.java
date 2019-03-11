/*
 * Copyright (C) 2019.
 * This file is part of project MonitoringUrls
 * Written by Sergiy Krasnikov <sergei29041996@gmail.com>
 */

package com.common;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Class for saving information about time (hour, minute and second).
 */
@Data
@AllArgsConstructor
public class TimeData {
  /** Number of hours. */
  private int hour;
  /** Number of minutes. */
  private int minute;
  /** Number of seconds. */
  private int second;
}
