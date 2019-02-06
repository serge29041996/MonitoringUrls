package com.common;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Class for min and max size of response.
 */
@Data
@AllArgsConstructor
public class SizeResponseData {
  private int minSizeResponse;
  private int maxSizeResponse;
}
