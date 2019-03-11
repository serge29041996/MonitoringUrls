/*
 * Copyright (C) 2019.
 * This file is part of project MonitoringUrls
 * Written by Sergiy Krasnikov <sergei29041996@gmail.com>
 */

package com.common;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Util class for saving time response for different statuses.
 */
@Data
@AllArgsConstructor
public class TimeResponseData {
  /** Max border of number seconds for status ok. */
  private long timeResponseOk;
  /** Max border of number seconds for status warning. */
  private long timeResponseWarning;
  /** Max border of number seconds for status critical. */
  private long timeResponseCritical;
}
