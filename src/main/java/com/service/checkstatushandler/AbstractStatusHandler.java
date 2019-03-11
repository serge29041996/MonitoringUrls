/*
 * Copyright (C) 2019.
 * This file is part of project MonitoringUrls
 * Written by Sergiy Krasnikov <sergei29041996@gmail.com>
 */

package com.service.checkstatushandler;

import com.common.StatusInfo;
import com.common.entities.StatusUrl;
import org.apache.http.HttpResponse;

/**
 * Abstract class of handler for checking status of url.
 */
public abstract class AbstractStatusHandler {
  /** Next handler for checking status of url. */
  private AbstractStatusHandler nextStatusHandler;
  /** String representation of status critical. */
  protected static final String STATUS_CRITICAL = "CRITICAL";

  /**
   * Link next handler for checking status of url.
   * @param nextStatusHandler next handler
   * @return next handler
   */
  public AbstractStatusHandler linkWithNextStatusHandler(AbstractStatusHandler nextStatusHandler) {
    this.nextStatusHandler = nextStatusHandler;
    return nextStatusHandler;
  }

  /**
   * Check parameters of url for defining status of url.
   * @param response http response, getting from url
   * @param statusUrl url, its parameters and status
   * @param statusInfo status and cause of status
   */
  public abstract void check(HttpResponse response, StatusUrl statusUrl, StatusInfo statusInfo);

  /**
   * Finding of extension next handler for checking status of url.
   * @param response http response, getting from url
   * @param statusUrl url, its parameters and status
   * @param statusInfo status and cause of status
   */
  protected void checkNext(HttpResponse response, StatusUrl statusUrl, StatusInfo statusInfo) {
    if (nextStatusHandler != null && !statusUrl.getStatus().equals(STATUS_CRITICAL)) {
      nextStatusHandler.check(response, statusUrl, statusInfo);
    }
  }
}
