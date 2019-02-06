package com.common;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Util class for saving time response for different statuses.
 */
@Data
@AllArgsConstructor
public class TimeResponseData {
  private long timeResponseOk;
  private long timeResponseWarning;
  private long timeResponseCritical;
}
