/*
 * Copyright (C) 2019.
 * This file is part of project MonitoringUrls
 * Written by Sergiy Krasnikov <sergei29041996@gmail.com>
 */

package com.common;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Class for min and max size of response.
 * It is used for initialization min and max sizes for parameters monitoring url constructor.
 */
@Data
@AllArgsConstructor
public class SizeResponseData {
  /** Min size of response. */
  private int minSizeResponse;
  /** Max size of response. */
  private int maxSizeResponse;
}
