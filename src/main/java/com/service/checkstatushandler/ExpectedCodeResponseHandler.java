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
 * Class for defining status url by expected code of response.
 * After defining status of url check is continue next handler
 */
public class ExpectedCodeResponseHandler extends AbstractStatusHandler {
  /**
   * Check parameters of url for defining status of url.
   * @param response http response, getting from url
   * @param statusUrl url, its parameters and status
   * @param statusInfo status and cause of status
   */
  @Override
  public void check(HttpResponse response, StatusUrl statusUrl, StatusInfo statusInfo) {
    int obtainedResponseStatusCode = response.getStatusLine().getStatusCode();
    if (obtainedResponseStatusCode != statusUrl
        .getParametersMonitoringUrl().getExpectedCodeResponse()) {
      statusUrl.setStatus(STATUS_CRITICAL);
      statusInfo.setCauseStatus("Response has unexpected http status code.");
    }
    checkNext(response, statusUrl, statusInfo);
  }
}
