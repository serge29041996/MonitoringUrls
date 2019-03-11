/*
 * Copyright (C) 2019.
 * This file is part of project MonitoringUrls
 * Written by Sergiy Krasnikov <sergei29041996@gmail.com>
 */

package com.service.checkstatushandler;

import static com.common.StringConverter.entityToString;

import com.common.StatusInfo;
import com.common.entities.StatusUrl;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;

/**
 * Class for definition status url with containing substring in response.
 */
public class StringInResponseHandler extends AbstractStatusHandler {
  /**
   * Check parameters of url for defining status of url.
   * @param response http response, getting from url
   * @param statusUrl url, its parameters and status
   * @param statusInfo status and cause of status
   */
  @Override
  public void check(HttpResponse response, StatusUrl statusUrl, StatusInfo statusInfo) {
    HttpEntity entity = response.getEntity();
    String contentResponseString = entityToString(entity, statusUrl, statusInfo);
    if (!contentResponseString.contains(statusUrl
        .getParametersMonitoringUrl().getSubstringResponse())) {
      statusUrl.setStatus(STATUS_CRITICAL);
      statusInfo.setCauseStatus("Response does not have need string");
    }
  }
}
