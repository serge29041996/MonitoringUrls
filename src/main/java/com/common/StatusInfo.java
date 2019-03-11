/*
 * Copyright (C) 2019.
 * This file is part of project MonitoringUrls
 * Written by Sergiy Krasnikov <sergei29041996@gmail.com>
 */

package com.common;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Class for saving status and information for cause status.
 */
@Data
@NoArgsConstructor
public class StatusInfo {
  /** Id of url, which status is checked. */
  private long idUrl;
  /** Received status for url. */
  private String status;
  /** Cause of received status (for all statuses with the exception of ok). */
  private String causeStatus;

  /**
   * Constructor with id of url.
   * @param idUrl id of url
   */
  public StatusInfo(long idUrl) {
    this.idUrl = idUrl;
    this.causeStatus = "";
  }
}
